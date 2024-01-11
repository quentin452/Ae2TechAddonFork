package com.fireball1725.ae2tech.container;

import com.fireball1725.ae2tech.container.slot.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class BaseContainer extends Container {
    final InventoryPlayer inventoryPlayer;
    final TileEntity tileEntity;
    public boolean isContainerValid = true;
    protected HashSet<Integer> locked = new HashSet();

    public BaseContainer(InventoryPlayer inventoryPlayer, TileEntity tileEntity) {
        this.inventoryPlayer = inventoryPlayer;
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        if (this.isContainerValid) {
            if ((this.tileEntity instanceof IInventory)) {
                return ((IInventory) this.tileEntity).isUseableByPlayer(entityPlayer);
            }
            return true;
        }
        return false;
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer, int offset_x, int offset_y) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.locked.contains(Integer.valueOf(j + i * 9 + 9))) {
                    addSlotToContainer(new SlotDisabled(inventoryPlayer, j + i * 9 + 9, 8 + j * 18 + offset_x, offset_y + i * 18));
                } else {
                    addSlotToContainer(new SlotPlayerInv(inventoryPlayer, j + i * 9 + 9, 8 + j * 18 + offset_x, offset_y + i * 18));
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            if (this.locked.contains(Integer.valueOf(i))) {
                addSlotToContainer(new SlotDisabled(inventoryPlayer, i, 8 + i * 18 + offset_x, 58 + offset_y));
            } else {
                addSlotToContainer(new SlotPlayerHotBar(inventoryPlayer, i, 8 + i * 18 + offset_x, 58 + offset_y));
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int idx) {
        ItemStack itemStack = null;
        AdvSlot clickSlot = (AdvSlot) this.inventorySlots.get(idx);
        if (clickSlot instanceof SlotDisabled) {
            return null;
        }

        if ((clickSlot != null) && (clickSlot.getHasStack())) {
            itemStack = clickSlot.getStack();
            if (itemStack == null) {
                return null;
            }

            List<Slot> selectedSlots = new ArrayList();
            if (clickSlot.isPlayerSide()) {
                //itemStack = shiftStoreItem(itemStack);
                for (int x = 0; x < this.inventorySlots.size(); x++) {
                    AdvSlot advSlot = (AdvSlot) this.inventorySlots.get(x);
                    if ((!advSlot.isPlayerSide()) && (!(advSlot instanceof SlotFake))) {
                        if (advSlot.isItemValid(itemStack)) {
                            selectedSlots.add(advSlot);
                        }
                    }
                }
            } else {
                for (int x = 0; x < this.inventorySlots.size(); x++) {
                    AdvSlot advSlot = (AdvSlot) this.inventorySlots.get(x);
                    if ((advSlot.isPlayerSide()) && (!(advSlot instanceof SlotFake))) {
                        if (advSlot.isItemValid(itemStack)) {
                            selectedSlots.add(advSlot);
                        }
                    }
                }
            }

            if ((selectedSlots.isEmpty()) && (clickSlot.isPlayerSide())) {
                if (itemStack != null) {
                    for (int x = 0; x < this.inventorySlots.size(); x++) {
                        AdvSlot advSlot = (AdvSlot) this.inventorySlots.get(x);
                        ItemStack dest = advSlot.getStack();
                        if ((!advSlot.isPlayerSide()) && (advSlot instanceof SlotFake)) {
                            if (dest == null) {
                                advSlot.putStack(itemStack != null ? itemStack.copy() : null);
                                advSlot.onSlotChanged();
                                updateSlot(advSlot);
                                return null;
                            }
                        }
                    }
                }
            }

            if (itemStack != null) {
                for (Slot d : selectedSlots) {
                    if ((!(d instanceof SlotDisabled))) {
                        if ((d.isItemValid(itemStack)) && (itemStack != null)) {
                            if (d.getHasStack()) {
                                ItemStack t = d.getStack();
                                if ((itemStack != null) && (itemStack.isItemEqual(t))) {
                                    int maxSize = t.getMaxStackSize();
                                    if (maxSize > d.getSlotStackLimit()) {
                                        maxSize = d.getSlotStackLimit();
                                    }
                                    int placeAble = maxSize - t.stackSize;
                                    if (itemStack.stackSize < placeAble) {
                                        placeAble = itemStack.stackSize;
                                    }
                                    t.stackSize += placeAble;
                                    itemStack.stackSize -= placeAble;
                                    if (itemStack.stackSize <= 0) {
                                        clickSlot.putStack(null);
                                        d.onSlotChanged();

                                        updateSlot(clickSlot);
                                        updateSlot(d);
                                        return null;
                                    }
                                    updateSlot(d);
                                }
                            }
                        }
                    }
                }

                for (Slot d : selectedSlots) {
                    if ((!(d instanceof SlotDisabled))) {
                        if ((d.isItemValid(itemStack)) && (itemStack != null)) {
                            if (d.getHasStack()) {
                                ItemStack t = d.getStack();
                                if ((itemStack != null) && (itemStack.isItemEqual(t))) {
                                    int maxSize = t.getMaxStackSize();
                                    if (maxSize > d.getSlotStackLimit()) {
                                        maxSize = d.getSlotStackLimit();
                                    }
                                    int placeAble = maxSize - t.stackSize;
                                    if (itemStack.stackSize < placeAble) {
                                        placeAble = itemStack.stackSize;
                                    }
                                    t.stackSize += placeAble;
                                    itemStack.stackSize -= placeAble;
                                    if (itemStack.stackSize <= 0) {
                                        clickSlot.putStack(null);
                                        d.onSlotChanged();

                                        updateSlot(clickSlot);
                                        updateSlot(d);
                                        return null;
                                    }
                                    updateSlot(d);
                                }
                            } else {
                                int maxSize = itemStack.getMaxStackSize();
                                if (maxSize > d.getSlotStackLimit()) {
                                    maxSize = d.getSlotStackLimit();
                                }
                                ItemStack tmp = itemStack.copy();
                                if (tmp.stackSize > maxSize) {
                                    tmp.stackSize = maxSize;
                                }
                                itemStack.stackSize -= tmp.stackSize;
                                d.putStack(tmp);
                                if (itemStack.stackSize <= 0) {
                                    clickSlot.putStack(null);
                                    d.onSlotChanged();

                                    updateSlot(clickSlot);
                                    updateSlot(d);
                                    return null;
                                }
                                updateSlot(d);
                            }
                        }
                    }
                }
            }

            clickSlot.putStack(itemStack != null ? itemStack.copy() : null);
        }
        updateSlot(clickSlot);
        return null;
    }

    private void updateSlot(Slot clickSlot) {
        detectAndSendChanges();
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }
}

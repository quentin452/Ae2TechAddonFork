package com.fireball1725.ae2tech.util;

import com.fireball1725.ae2tech.helpers.IInternalInventory;
import com.fireball1725.ae2tech.helpers.InventoryOperation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Iterator;

public class InternalInventory implements IInventory, Iterable<ItemStack> {
    public boolean enableClientEvents = false;
    protected IInternalInventory te;
    protected int size;
    protected int maxStack;
    protected ItemStack[] inv;

//    public IMEInventory getIMEI() {
//        return new MEInventoryWrapper(this, null);
//    }

    public InternalInventory(IInternalInventory _te, int s) {
        this.te = _te;
        this.size = s;
        this.maxStack = 64;
        this.inv = new ItemStack[s];
    }

    public boolean isEmpty() {
        for (int x = 0; x < getSizeInventory(); x++) {
            if (getStackInSlot(x) != null) {
                return false;
            }
        }
        return true;
    }

    protected boolean eventsEnabled() {
        return (Platform.isServer()) || (this.enableClientEvents);
    }

    public void setMaxStackSize(int s) {
        this.maxStack = s;
    }

    public ItemStack getStackInSlot(int var1) {
        return this.inv[var1];
    }

    public ItemStack decrStackSize(int slot, int qty) {
        if (this.inv[slot] != null) {
            ItemStack split = getStackInSlot(slot);
            ItemStack ns = null;
            if (qty >= split.stackSize) {
                ns = this.inv[slot];
                this.inv[slot] = null;
            } else {
                ns = split.splitStack(qty);
            }
            if ((this.te != null) && (eventsEnabled())) {
                this.te.onChangeInventory(this, slot, InventoryOperation.decrStackSize, ns, null);
            }
            markDirty();
            return ns;
        }
        return null;
    }

    public ItemStack getStackInSlotOnClosing(int var1) {
        return null;
    }

    public void setInventorySlotContents(int slot, ItemStack newItemStack) {
        ItemStack oldStack = this.inv[slot];
        this.inv[slot] = newItemStack;
        if ((this.te != null) && (eventsEnabled())) {
            ItemStack removed = oldStack;
            ItemStack added = newItemStack;
            if ((oldStack != null) && (newItemStack != null)) { //TODO: Check if same item
                if (oldStack.stackSize > newItemStack.stackSize) {
                    removed = removed.copy();
                    removed.stackSize -= newItemStack.stackSize;
                    added = null;
                } else if (oldStack.stackSize < newItemStack.stackSize) {
                    added = added.copy();
                    added.stackSize -= oldStack.stackSize;
                    removed = null;
                } else {
                    removed = added = null;
                }
            }
            this.te.onChangeInventory(this, slot, InventoryOperation.setInventorySlotContents, removed, added);
            markDirty();
        }
    }

    public void markDirty() {
        if ((this.te != null) && (eventsEnabled())) {
            this.te.onChangeInventory(this, -1, InventoryOperation.markDirty, null, null);
        }
    }

    public void markDirty(int slotIndex) {
        if ((this.te != null) && (eventsEnabled())) {
            this.te.onChangeInventory(this, slotIndex, InventoryOperation.markDirty, null, null);
        }
    }

    public int getInventoryStackLimit() {
        return this.maxStack > 64 ? 64 : this.maxStack;
    }

    public boolean isUseableByPlayer(EntityPlayer var1) {
        return true;
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

    public void writeToNBT(NBTTagCompound target) {
        for (int x = 0; x < this.size; x++) {
            try {
                NBTTagCompound c = new NBTTagCompound();
                if (this.inv[x] != null) {
                    this.inv[x].writeToNBT(c);
                }
                target.setTag("#" + x, c);
            } catch (Exception e) {
            }
        }
    }

    public void readFromNBT(NBTTagCompound target) {
        for (int x = 0; x < this.size; x++) {
            try {
                NBTTagCompound c = target.getCompoundTag("#" + x);
                if (c != null) {
                    this.inv[x] = ItemStack.loadItemStackFromNBT(c);
                }
            } catch (Exception e) {
                LogHelper.error(e);
            }
        }
    }

    public void writeToNBT(NBTTagCompound data, String name) {
        NBTTagCompound c = new NBTTagCompound();
        writeToNBT(c);
        data.setTag(name, c);
    }

    public void readFromNBT(NBTTagCompound data, String name) {
        NBTTagCompound c = data.getCompoundTag(name);
        if (c != null) {
            readFromNBT(c);
        }
    }

    public int getSizeInventory() {
        return this.size;
    }

    public String getInventoryName() {
        return "internal";
    }

    public boolean hasCustomInventoryName() {
        return false;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return true;
    }

    public Iterator<ItemStack> iterator() {
        return new InvIterator(this);
    }
}

package com.fireball1725.ae2tech.tileentity;

import com.fireball1725.ae2tech.events.TileEventHandler;
import com.fireball1725.ae2tech.events.TileEventType;
import com.fireball1725.ae2tech.helpers.IInternalInventory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityAEBaseInventory extends TileEntityAEBase implements ISidedInventory, IInternalInventory {
    public TileEntityAEBaseInventory() {
        addNewHandler(new TileEventHandler(new TileEventType[]{TileEventType.WORLD_NBT}) {
            public void readFromNBT(NBTTagCompound data) {
                IInventory inventory = TileEntityAEBaseInventory.this.getInternalInventory();
                NBTTagCompound opt = data.getCompoundTag("inv");
                for (int x = 0; x < inventory.getSizeInventory(); x++) {
                    NBTTagCompound item = opt.getCompoundTag("item" + x);
                    inventory.setInventorySlotContents(x, ItemStack.loadItemStackFromNBT(item));
                }
            }

            public void writeToNBT(NBTTagCompound data) {
                IInventory inventory = TileEntityAEBaseInventory.this.getInternalInventory();
                NBTTagCompound opt = new NBTTagCompound();
                for (int x = 0; x < inventory.getSizeInventory(); x++) {
                    NBTTagCompound item = new NBTTagCompound();
                    ItemStack itemStack = TileEntityAEBaseInventory.this.getStackInSlot(x);
                    if (itemStack != null) {
                        itemStack.writeToNBT(item);
                    }
                    opt.setTag("item" + x, item);
                }
                data.setTag("inv", opt);
            }
        });
    }

    public abstract IInventory getInternalInventory();

    public int getSizeInventory() {
        return getInternalInventory().getSizeInventory();
    }

    public ItemStack getStackInSlot(int i) {
        return getInternalInventory().getStackInSlot(i);
    }

    public ItemStack decrStackSize(int i, int j) {
        return getInternalInventory().decrStackSize(i, j);
    }

    public ItemStack getStackInSlotOnClosing(int i) {
        return null;
    }

    public void setInventorySlotContents(int i, ItemStack itemStack) {
        getInternalInventory().setInventorySlotContents(i, itemStack);
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer p) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return true;
    }

    public boolean canInsertItem(int i, ItemStack itemStack, int j) {
        return isItemValidForSlot(i, itemStack);
    }

    public boolean canExtractItem(int i, ItemStack itemStack, int j) {
        return true;
    }

    public int[] getAccessibleSlotsFromSide(int side) {
        Block block = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
        // TODO: Check block insance of one of my blocks, then return something
        return null;
    }

    public String getInventoryName() {
        return getCustomName();
    }

    public boolean hasCustomInventoryName() {
        return hasCustomName();
    }
}

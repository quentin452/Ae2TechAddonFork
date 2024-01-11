package com.fireball1725.ae2tech.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotFake extends AdvSlot {
    int invSlot;

    public SlotFake(IInventory iInventory, int idx, int x, int y) {
        super(iInventory, idx, x, y);
        this.invSlot = idx;
    }

    public boolean canTakeStack(EntityPlayer entityPlayer) {
        return false;
    }

    public ItemStack desrStackSize(int par1) {
        return null;
    }

    public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemStack) {

    }

    public void putStack(ItemStack itemStack) {
        if (itemStack != null) {
            itemStack = itemStack.copy();
        }
        super.putStack(itemStack);
    }

    public boolean isItemValid(ItemStack itemStack) {
        return false;
    }
}

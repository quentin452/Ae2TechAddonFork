package com.fireball1725.ae2tech.helpers;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public abstract interface IInternalInventory {
    public abstract void saveChanges();

    public abstract void onChangeInventory(IInventory paramIInventory, int paramInt, InventoryOperation paramInvOperation, ItemStack paramItemStack1, ItemStack paramItemStack2);
}

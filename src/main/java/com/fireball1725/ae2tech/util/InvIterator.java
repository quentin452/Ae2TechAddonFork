package com.fireball1725.ae2tech.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

public class InvIterator implements Iterator<ItemStack> {
    final IInventory inv;
    final int size;
    int x = 0;

    public InvIterator(IInventory i) {
        this.inv = i;
        this.size = this.inv.getSizeInventory();
    }

    public boolean hasNext() {
        return this.x < this.size;
    }

    public ItemStack next() {
        return this.inv.getStackInSlot(this.x++);
    }

    public void remove() {
        throw new RuntimeException("Sorry, cant do that :D");
    }

}

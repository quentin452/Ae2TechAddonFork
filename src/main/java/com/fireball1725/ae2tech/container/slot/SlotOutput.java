package com.fireball1725.ae2tech.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotOutput extends AdvSlot {
    public SlotOutput(IInventory iInventory, int idx, int x, int y) {
        super(iInventory, idx, x, y);
        //this.IIcon = 1;
    }

    public boolean isItemValid(ItemStack itemStack) {
        return false;
    }
}

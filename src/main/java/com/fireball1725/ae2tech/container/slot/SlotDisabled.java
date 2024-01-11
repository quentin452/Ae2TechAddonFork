package com.fireball1725.ae2tech.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotDisabled extends AdvSlot {
    public SlotDisabled(IInventory iInventory, int idx, int x, int y) {
        super(iInventory, idx, x, y);
    }

    public boolean isItemValid(ItemStack itemStack) {
        return false;
    }

    public boolean canTakeStack(EntityPlayer entityPlayer) {
        return false;
    }
}

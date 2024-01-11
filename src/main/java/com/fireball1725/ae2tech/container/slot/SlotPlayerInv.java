package com.fireball1725.ae2tech.container.slot;

import net.minecraft.inventory.IInventory;

public class SlotPlayerInv extends AdvSlot {
    public SlotPlayerInv(IInventory iInventory, int idx, int x, int y) {
        super(iInventory, idx, x, y);
        this.isPlayerSide = true;
    }
}

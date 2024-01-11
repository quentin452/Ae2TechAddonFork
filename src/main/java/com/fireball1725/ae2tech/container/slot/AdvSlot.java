package com.fireball1725.ae2tech.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class AdvSlot extends Slot {
    public int IIcon = -1;
    public int defX;
    public int defY;
    public boolean isDraggable = true;
    public boolean isPlayerSide = false;
    public boolean isDisplay = false;

    public AdvSlot(IInventory iInventory, int idx, int x, int y) {
        super(iInventory, idx, x, y);
        this.defX = x;
        this.defY = y;
    }

    public Slot setNotDraggable() {
        this.isDraggable = false;
        return this;
    }

    public Slot setPlayerSide() {
        this.isPlayerSide = true;
        return this;
    }

    @Override
    public boolean func_111238_b() {
        return isEnabled();
    }

    public boolean isEnabled() {
        return true;
    }

    public String getTooltip() {
        return null;
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
    }

    @Override
    public ItemStack getStack() {
        if (!isEnabled()) {
            return null;
        }
        if (this.inventory.getSizeInventory() <= getSlotIndex()) {
            return null;
        }
        if (this.isDisplay) {
            this.isDisplay = false;
            return getDisplayStack();
        }
        return super.getStack();
    }

    @Override
    public void putStack(ItemStack itemStack) {
        if (isEnabled()) {
            super.putStack(itemStack);
        }
    }

    public void clearStack() {
        super.putStack(null);
    }

    @Override
    public boolean canTakeStack(EntityPlayer entityPlayer) {
        if (isEnabled()) {
            return super.canTakeStack(entityPlayer);
        }
        return false;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        if (isEnabled()) {
            return super.isItemValid(itemStack);
        }
        return false;
    }

    public ItemStack getDisplayStack() {
        return super.getStack();
    }

    public float getOpacityOfIcon() {
        return 0.4F;
    }

    public boolean renderIconWithItem() {
        return false;
    }

    public int getIcon() {
        return this.IIcon;
    }

    public boolean isPlayerSide() {
        return this.isPlayerSide;
    }
}

package com.fireball1725.ae2tech.container.slot;

import appeng.api.AEApi;
import appeng.api.IAppEngApi;
import appeng.api.crafting.ICraftingPatternMAC;
import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.implementations.items.IUpgradeModule;
import com.fireball1725.ae2tech.util.Platform;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SlotRestrictedInput extends AdvSlot {
    public PlaceableItemType which;
    public boolean allowEdit = true;
    public int stackLimit = -1;

    public SlotRestrictedInput(PlaceableItemType valid, IInventory iInventory, int slotNum, int x, int y) {
        super(iInventory, slotNum, x, y);
        this.which = valid;
        this.IIcon = valid.IIcon;
    }

    public int getSlotStackLimit() {
        if (this.stackLimit != -1) {
            return this.stackLimit;
        }
        return super.getSlotStackLimit();
    }

    public boolean isValid(ItemStack itemStack, World world) {
        if (this.which == PlaceableItemType.VALID_ENCODED_PATTERN_W_OUPUT) {
            ICraftingPatternMAC ap = (itemStack.getItem() instanceof ICraftingPatternItem) ? ((ICraftingPatternItem) itemStack.getItem()).getPatternForItem(itemStack) : null;
            if ((ap != null) && (ap.isEncoded()) && (ap.isCraftable(world))) {
                return true;
            }
            return false;
        }
        return true;
    }

    public boolean canTakeStack(EntityPlayer entityPlayer) {
        return this.allowEdit;
    }

    public Slot setStackLimit(int i) {
        this.stackLimit = i;
        return this;
    }

    public ItemStack getDisplayStack() {
        if ((Platform.isClient()) && ((this.which == PlaceableItemType.VALID_ENCODED_PATTERN_W_OUPUT) || (this.which == PlaceableItemType.ENCODED_PATTERN_W_OUTPUT))) {
            ItemStack itemStack = super.getStack();
            if (itemStack != null) {
                ICraftingPatternMAC ap = (itemStack.getItem() instanceof ICraftingPatternItem) ? ((ICraftingPatternItem) itemStack.getItem()).getPatternForItem(itemStack) : null;
                if (ap != null) {
                    return ap.getOutput().getItemStack();
                }
            }
        }
        return super.getStack();
    }

    public boolean isItemValid(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        if (itemStack.getItem() == null) {
            return false;
        }

        if (!this.inventory.isItemValidForSlot(getSlotIndex(), itemStack)) {
            return false;
        }

        IAppEngApi api = AEApi.instance();

        if (!this.allowEdit) {
            return false;
        }

        switch (this.which) {
            case UPGRADES:
                return ((itemStack.getItem() instanceof IUpgradeModule));
            case ENCODED_PATTERN_W_OUTPUT:
                return false;
            case VALID_ENCODED_PATTERN_W_OUPUT:
                return false;
        }

        return false;
    }

    public static enum PlaceableItemType {
        UPGRADES(223), VALID_ENCODED_PATTERN_W_OUPUT(127), ENCODED_PATTERN_W_OUTPUT(127),;

        public final int IIcon;

        private PlaceableItemType(int o) {
            this.IIcon = o;
        }
    }
}

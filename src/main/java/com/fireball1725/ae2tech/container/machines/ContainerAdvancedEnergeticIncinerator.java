package com.fireball1725.ae2tech.container.machines;

import com.fireball1725.ae2tech.container.BaseContainer;
import com.fireball1725.ae2tech.container.slot.SlotDisabled;
import com.fireball1725.ae2tech.container.slot.SlotNormal;
import com.fireball1725.ae2tech.container.slot.SlotOutput;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerAdvancedEnergeticIncinerator extends BaseContainer {
    IInventory inventory;

    public ContainerAdvancedEnergeticIncinerator(InventoryPlayer inventoryPlayer, TileEntity tileEntity) {
        super(inventoryPlayer, tileEntity);
        this.inventory = (IInventory) tileEntity;

        // Pattern Card = Slots #0 - 8
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new SlotDisabled(inventory, i, 8 + 18 * i, 70));
        }

        // Import Bus = Slot #9
        addSlotToContainer(new SlotDisabled(inventory, 9, 8 + 18, 28));

        // Item to Smelt = Slot #10
        addSlotToContainer(new SlotNormal(inventory, 10, 8 + 18 * 4, 28));

        // Output item = Slot #11
        addSlotToContainer(new SlotOutput(inventory, 11, 8 + 18 * 7, 28));

        // Upgrade Slots = Slot #12, 13, 14, 15
        addSlotToContainer(new SlotDisabled(inventory, 12, 187, 8));
        addSlotToContainer(new SlotDisabled(inventory, 13, 187, 8 + 18));
        addSlotToContainer(new SlotDisabled(inventory, 14, 187, 8 + 18 * 2));
        addSlotToContainer(new SlotDisabled(inventory, 15, 187, 8 + 18 * 3));

        // Bind Player Slots
        bindPlayerInventory(inventoryPlayer, 0, getHeight() - 82);
    }

    protected int getHeight() {
        return 184;
    }

}

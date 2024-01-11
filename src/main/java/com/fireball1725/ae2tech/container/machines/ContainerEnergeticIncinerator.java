package com.fireball1725.ae2tech.container.machines;

import com.fireball1725.ae2tech.container.BaseContainer;
import com.fireball1725.ae2tech.container.slot.SlotNormal;
import com.fireball1725.ae2tech.container.slot.SlotOutput;
import com.fireball1725.ae2tech.container.slot.SlotRestrictedInput;
import com.fireball1725.ae2tech.tileentity.machines.TileEntityEnergeticIncinerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerEnergeticIncinerator extends BaseContainer {
    public int progress = -1;
    public int upgradeTier = 0;
    IInventory inventory;
    TileEntityEnergeticIncinerator tileEntityEnergeticIncinerator;

    public ContainerEnergeticIncinerator(InventoryPlayer inventoryPlayer, TileEntity tileEntity) {
        super(inventoryPlayer, tileEntity);
        this.inventory = (IInventory) tileEntity;
        this.tileEntityEnergeticIncinerator = (TileEntityEnergeticIncinerator) tileEntity;

        // Item to Smelt = Slot #0
        addSlotToContainer(new SlotNormal(inventory, 0, 8 + 18 * 4, 28));

        // Output item = Slot #1
        addSlotToContainer(new SlotOutput(inventory, 1, 8 + 18 * 7, 28));

        // Upgrade Slot #1 & #2
        addSlotToContainer(new SlotRestrictedInput(SlotRestrictedInput.PlaceableItemType.UPGRADES, inventory, 2, 187, 8).setStackLimit(1));
        addSlotToContainer(new SlotRestrictedInput(SlotRestrictedInput.PlaceableItemType.UPGRADES, inventory, 3, 187, 8 + 18).setStackLimit(1));

        // Bind Player Slots
        bindPlayerInventory(inventoryPlayer, 0, getHeight() - 82);
    }

    protected int getHeight() {
        return 152;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

            if (this.progress != this.tileEntityEnergeticIncinerator.furnaceCookTime) {
                icrafting.sendProgressBarUpdate(this, 0, this.tileEntityEnergeticIncinerator.furnaceCookTime);
                icrafting.sendProgressBarUpdate(this, 1, this.tileEntityEnergeticIncinerator.getUpgradeTier());
            }
        }

        this.progress = this.tileEntityEnergeticIncinerator.furnaceCookTime;
        this.upgradeTier = this.tileEntityEnergeticIncinerator.getUpgradeTier();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
        if (p_75137_1_ == 0) {
            this.tileEntityEnergeticIncinerator.furnaceCookTime = p_75137_2_;
            this.progress = p_75137_2_;
        }

        if (p_75137_1_ == 1) {
            this.upgradeTier = p_75137_2_;
        }
    }
}

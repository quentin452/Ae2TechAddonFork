package com.fireball1725.ae2tech.container.machines;

import com.fireball1725.ae2tech.container.BaseContainer;
import com.fireball1725.ae2tech.container.slot.SlotNormal;
import com.fireball1725.ae2tech.container.slot.SlotOutput;
import com.fireball1725.ae2tech.tileentity.machines.TileEntityEnergeticCrumbler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerEnergeticCrumbler extends BaseContainer {
    public int progress = -1;
    public int test = 0;
    IInventory inventory;
    TileEntityEnergeticCrumbler tileEntityEnergeticCrumbler;

    public ContainerEnergeticCrumbler(InventoryPlayer inventoryPlayer, TileEntity tileEntity) {
        super(inventoryPlayer, tileEntity);
        this.inventory = (IInventory) tileEntity;
        this.tileEntityEnergeticCrumbler = (TileEntityEnergeticCrumbler) tileEntity;

        // Item to Smelt = Slot #0
        addSlotToContainer(new SlotNormal(inventory, 0, 8 + 18 * 2, 28));

        // Output item = Slot #1
        addSlotToContainer(new SlotOutput(inventory, 1, 8 + 18 * 5, 28));
        addSlotToContainer(new SlotOutput(inventory, 2, 8 + 18 * 6, 28));
        addSlotToContainer(new SlotOutput(inventory, 3, 8 + 18 * 7, 28));

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

            //if (this.progress != this.tileEntityEnergeticCrumbler.furnaceCookTime) {
            //    icrafting.sendProgressBarUpdate(this, 0, this.tileEntityEnergeticCrumbler.furnaceCookTime);
            //}
        }

        //this.progress = this.tileEntityEnergeticCrumbler.furnaceCookTime;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
        if (p_75137_1_ == 0) {
            //this.tileEntityEnergeticCrumbler.furnaceCookTime = p_75137_2_;
            //this.progress = p_75137_2_;
            //this.test = tileEntityEnergeticCrumbler.getCookProgressPercent();
        }
    }
}

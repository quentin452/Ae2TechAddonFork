package com.fireball1725.ae2tech.tileentity.machines;

import appeng.api.AEApi;
import appeng.api.implementations.items.IUpgradeModule;
import appeng.api.networking.GridFlags;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.MachineSource;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import com.fireball1725.ae2tech.blocks.Blocks;
import com.fireball1725.ae2tech.events.TileEventHandler;
import com.fireball1725.ae2tech.events.TileEventType;
import com.fireball1725.ae2tech.helpers.InventoryOperation;
import com.fireball1725.ae2tech.items.Items;
import com.fireball1725.ae2tech.reference.Settings;
import com.fireball1725.ae2tech.tileentity.TileEntityAEBaseNetworkInventory;
import com.fireball1725.ae2tech.util.InternalInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityEnergeticIncinerator extends TileEntityAEBaseNetworkInventory implements ISidedInventory {
    private static final int[] slotsTop = new int[]{0};
    private static final int[] slotsBottom = new int[]{1};
    private static final int[] slotsSide = new int[]{0, 1};
    public int furnaceCookTime = 0;
    int machineState = 0;
    private InternalInventory internalInventory = new InternalInventory(this, 4);
    private BaseActionSource mySrc;
    private boolean isActive = false;
    private boolean isPowered = false;
    private boolean isWorking = false;
    private int upgradeTier = 0;
    private boolean performanceUpgrade = false;

    // Notes:
    // 150 Ticks per item
    // 2 AE/t Idle Draw
    // 1 AE/t when smelting

    // Each upgrade (takes 25 ticks off time, also doubles AE/t when smelting
    // upgr speed   idle    working total cost per item
    // 0    150t    2ae/t   1ae/t   150ae per item  <--- Default w/ no upgrades
    // 1    125t    4ae/t   2ae/t   250ae per item
    // 2    100t    6ae/t   4ae/t   400ae per item
    // 3    75t     8ae/t   8ae/t   600ae per item
    // 4    50t     10ae/t  16ae/t  800ae per item
    // 5    25t     12ae/t  32ae/t  800ae per item

    public TileEntityEnergeticIncinerator() {
        this.mySrc = new MachineSource(this);
        this.gridProxy.setFlags(new GridFlags[]{});
        this.gridProxy.setIdlePowerUsage(Settings.MACHINE_INCINERATOR_POWER_IDLE);
        this.gridProxy.setVisualRepresentation(new ItemStack(Blocks.MACHINE_ENERGETICINCINERATOR.block));
        addNewHandler(new invManager());
    }

    @Override
    public IInventory getInternalInventory() {
        return this.internalInventory;
    }

    @MENetworkEventSubscribe
    public void powerRender(MENetworkPowerStatusChange event) {
        this.isActive = this.gridProxy.isActive();
        this.isPowered = this.gridProxy.isPowered();
        updateMachineState();
        updateLight();
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection dir) {
        return AECableType.SMART;
    }

    @Override
    public void onChangeInventory(IInventory paramIInventory, int paramInt, InventoryOperation paramInvOperation, ItemStack paramItemStack1, ItemStack paramItemStack2) {

        if (paramInt == -1) {
            return;
        }

        // We dont care about slot 0 or 1
        if (paramInt < 2) {
            return;
        }

        if (paramItemStack1 != null && paramItemStack1.getItem() != null) {
            if (paramItemStack1.getItem().equals(Items.CARD_MACHINEPERFUPGRADE.item)) {
                this.upgradeTier = 0;
                this.performanceUpgrade = false;
                this.gridProxy.setIdlePowerUsage(Settings.MACHINE_INCINERATOR_POWER_IDLE);
                updateMachineState();
                updateLight();
            }
        }

        if (paramItemStack2 != null && paramItemStack2.getItem() != null) {
            if (paramItemStack2.getItem().equals(Items.CARD_MACHINEPERFUPGRADE.item)) {
                this.upgradeTier = paramItemStack2.getItemDamage();
                this.performanceUpgrade = true;
                double powerUsage = Settings.MACHINE_INCINERATOR_POWER_IDLE + ((upgradeTier + 1) * Settings.PERFORMANCE_UPGRADE_POWER_MULTIPLIER);
                this.gridProxy.setIdlePowerUsage(powerUsage);
                updateMachineState();
                updateLight();
            }
        }
    }

    private void processFurnace() {
        if (!this.worldObj.isRemote) {
            if (this.isPowered && this.canSmelt()) {
                ++furnaceCookTime;

                if (!this.isWorking) {
                    this.isWorking = true;
                    updateMachineState();
                    updateLight();
                }

                // TODO: Pull power from the grid...

                int maxFurnaceCookTime = 150;

                if (performanceUpgrade) {
                    maxFurnaceCookTime = maxFurnaceCookTime - ((upgradeTier + 1) * 25);
                }

                if (this.furnaceCookTime >= maxFurnaceCookTime) {
                    this.furnaceCookTime = 0;
                    this.smeltItem();
                    this.markDirty();
                }
            } else {
                this.furnaceCookTime = 0;
                if (this.isWorking) {
                    this.isWorking = false;
                    updateMachineState();
                    updateLight();
                }
            }
        }
    }

    private boolean canSmelt() {
        if (this.internalInventory.getStackInSlot(0) == null) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.internalInventory.getStackInSlot(0));
            if (itemstack == null) return false;
            if (this.internalInventory.getStackInSlot(1) == null) return true;
            if (!this.internalInventory.getStackInSlot(1).isItemEqual(itemstack)) return false;
            int result = this.internalInventory.getStackInSlot(1).stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.internalInventory.getStackInSlot(1).getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.internalInventory.getStackInSlot(0));

            if (this.internalInventory.getStackInSlot(1) == null) {
                this.internalInventory.setInventorySlotContents(1, itemstack.copy());
            } else if (this.internalInventory.getStackInSlot(1).getItem() == itemstack.getItem()) {
                this.internalInventory.getStackInSlot(1).stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            --this.internalInventory.getStackInSlot(0).stackSize;

            if (this.internalInventory.getStackInSlot(0).stackSize <= 0) {
                this.internalInventory.setInventorySlotContents(0, null);
            }
        }
    }

    public boolean isPowered() {
        return isPowered;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isWorking() {
        return isWorking;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        this.internalInventory.setInventorySlotContents(i, itemStack);

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        if (i == 0) {
            return true;
        }

        if (i == 1) {
            return false;
        }

        if (itemStack != null) {
            if (itemStack.getItem() instanceof IUpgradeModule) {
                if (itemStack.getItem().equals(AEApi.instance().materials().materialCardRedstone.item())) {
                    if ((internalInventory.getStackInSlot(2) != null && internalInventory.getStackInSlot(2).getItem().equals(AEApi.instance().materials().materialCardRedstone.item())) || (internalInventory.getStackInSlot(3) != null && internalInventory.getStackInSlot(3).getItem().equals(AEApi.instance().materials().materialCardRedstone.item()))) {
                        return false;
                    }

                    return true;
                }
                if (itemStack.getItem().equals(Items.CARD_MACHINEPERFUPGRADE.item)) {
                    if ((internalInventory.getStackInSlot(2) != null && internalInventory.getStackInSlot(2).getItem().equals(Items.CARD_MACHINEPERFUPGRADE.item)) || (internalInventory.getStackInSlot(3) != null && internalInventory.getStackInSlot(3).getItem().equals(Items.CARD_MACHINEPERFUPGRADE.item))) {
                        return false;
                    }

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int j) {
        return this.isItemValidForSlot(i, itemStack);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, int j) {
        return i != 0;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return side == 0 ? slotsBottom : (side == 1 ? slotsTop : slotsSide);
    }

    public int getState() {
        return machineState;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        int oldMachineState = this.machineState;

        isActive = nbtTagCompound.getBoolean("isActive");
        isPowered = nbtTagCompound.getBoolean("isPowered");
        isWorking = nbtTagCompound.getBoolean("isWorking");
        furnaceCookTime = nbtTagCompound.getInteger("cookTime");
        machineState = nbtTagCompound.getInteger("machineState");
        upgradeTier = nbtTagCompound.getInteger("upgradeTier");

        if (oldMachineState != machineState) {
            updateLight();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean("isActive", isActive);
        nbtTagCompound.setBoolean("isPowered", isPowered);
        nbtTagCompound.setBoolean("isWorking", isWorking);
        nbtTagCompound.setInteger("cookTime", furnaceCookTime);
        nbtTagCompound.setInteger("machineState", machineState);
        nbtTagCompound.setInteger("upgradeTier", upgradeTier);
    }

    public void updateMachineState() {
        machineState = !isPowered ? 0 : !isWorking ? 1 : 2;

        try {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        } catch (Exception e) {
        }
    }

    public void updateLight() {
        try {
            if (worldObj.provider.hasNoSky) {
                worldObj.updateLightByType(EnumSkyBlock.Sky, xCoord, yCoord, zCoord);
            }

            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
        } catch (Exception e) {
        }

    }

    public int getUpgradeTier() {
        return upgradeTier;
    }

    private class invManager extends TileEventHandler {
        public invManager() {
            super(TileEventType.WORLD_NBT, TileEventType.TICK);
        }

        @Override
        public void Tick() {
            processFurnace();
            super.Tick();
        }
    }
}

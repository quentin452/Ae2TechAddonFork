package com.fireball1725.ae2tech.tileentity.machines;

import appeng.api.implementations.tiles.ICrankable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.events.MENetworkChannelChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.MachineSource;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import com.fireball1725.ae2tech.blocks.Blocks;
import com.fireball1725.ae2tech.events.TileEventHandler;
import com.fireball1725.ae2tech.helpers.IMachine;
import com.fireball1725.ae2tech.helpers.InventoryOperation;
import com.fireball1725.ae2tech.tileentity.TileEntityAEBaseNetworkInventory;
import com.fireball1725.ae2tech.util.InternalInventory;
import com.fireball1725.ae2tech.util.LogHelper;
import com.fireball1725.ae2tech.util.Platform;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityEnergeticCrumbler extends TileEntityAEBaseNetworkInventory implements IMachine, ICrankable {
    public int furnaceCookTime;
    private int state = 0;
    private InternalInventory internalInventory = new InternalInventory(this, 2);
    private BaseActionSource mySrc;

    public TileEntityEnergeticCrumbler() {
        this.mySrc = new MachineSource(this);
        this.gridProxy.setFlags(new GridFlags[]{GridFlags.REQUIRE_CHANNEL});
        this.gridProxy.setIdlePowerUsage(2.0D);
        this.gridProxy.setVisualRepresentation(new ItemStack(Blocks.MACHINE_ENERGETICINCINERATOR.block));
        this.addNewHandler(new testHandler());
    }

    @Override
    public IInventory getInternalInventory() {
        return this.internalInventory;
    }

    @MENetworkEventSubscribe
    public void powerRender(MENetworkPowerStatusChange event) {
        LogHelper.debug(">> MENetworkPowerStatusChange");
    }

    @MENetworkEventSubscribe
    public void channelRender(MENetworkChannelChanged event) {
        LogHelper.debug(">> MENetworkChannelChanged");
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection dir) {
        return AECableType.SMART;
    }


    //TODO: FIX
    public int getCookProgressPercent() {
        return 0;
    }

    @Override
    public boolean isProcessing() {
        if (Platform.isClient()) {
            return (this.state > 1);
        }
        return false;
    }

    @Override
    public int getState() {
        LogHelper.debug("Is Powered: " + this.gridProxy.isActive());
        return 1;
        //return state;
    }

    @Override
    public void onChangeInventory(IInventory paramIInventory, int paramInt, InventoryOperation paramInvOperation, ItemStack paramItemStack1, ItemStack paramItemStack2) {

    }

    @Override
    public boolean canTurn() {
        return true;
    }

    @Override
    public void applyTurn() {

    }

    @Override
    public boolean canCrankAttach(ForgeDirection directionToCrank) {
        return true;
    }

    @Override
    public Packet getDescriptionPacket() {
        this.onReady();
        return super.getDescriptionPacket();
    }

    private class testHandler extends TileEventHandler {
        @Override
        public void writeToNBT(NBTTagCompound data) {
            super.writeToNBT(data);
            LogHelper.debug("WTF????");
        }
    }
}

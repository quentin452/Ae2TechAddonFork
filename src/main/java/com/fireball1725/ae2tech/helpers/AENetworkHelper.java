package com.fireball1725.ae2tech.helpers;

import appeng.api.AEApi;
import appeng.api.networking.*;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.events.MENetworkPowerIdleChange;
import appeng.api.networking.pathing.IPathingGrid;
import appeng.api.networking.security.ISecurityGrid;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.networking.ticking.ITickManager;
import appeng.api.util.AEColor;
import appeng.api.util.DimensionalCoord;
import com.fireball1725.ae2tech.events.TickHandler;
import com.fireball1725.ae2tech.tileentity.TileEntityAEBase;
import com.fireball1725.ae2tech.util.LogHelper;
import com.fireball1725.ae2tech.util.Platform;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Collections;
import java.util.EnumSet;

public class AENetworkHelper implements IGridBlock {

    private final IGridProxyable iGridProxyable;
    private final String nbtName;
    private final boolean worldNode;
    public AEColor aeColor = AEColor.Transparent;
    private IGridNode iGridNode = null;
    private NBTTagCompound data = null;
    private boolean isReady = false;
    private EnumSet<ForgeDirection> validSides;
    private double idleDraw = 0.0D;
    private ItemStack itemStackVisual;
    private EnumSet<GridFlags> gridFlags = EnumSet.noneOf(GridFlags.class);

    public AENetworkHelper(IGridProxyable iGridProxyable, String nbtName, ItemStack visual, boolean inWorld) {
        this.iGridProxyable = iGridProxyable;
        this.nbtName = nbtName;
        this.worldNode = inWorld;
        this.itemStackVisual = visual;
        this.validSides = EnumSet.allOf(ForgeDirection.class);
    }

    public ItemStack getMachineRepresentation() {
        return this.itemStackVisual;
    }

    public void setVisualRepresentation(ItemStack itemStack) {
        this.itemStackVisual = itemStack;
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        LogHelper.debug("GridProxy - Write to NBT");
        if (this.iGridNode != null) {
            this.iGridNode.saveToNBT(this.nbtName, nbtTagCompound);
        }
    }

    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        LogHelper.debug("GridProxy - Read from NBT");
        this.data = nbtTagCompound;
        if ((this.iGridNode != null) && (this.data != null)) {
            this.iGridNode.loadFromNBT(this.nbtName, this.data);
            this.data = null;
        }
    }

    public void setNetworkStatus(IGrid iGrid, int channelsInUse) {
    }

    public DimensionalCoord getLocation() {
        return this.iGridProxyable.getLocation();
    }

    public AEColor getGridColor() {
        return this.aeColor;
    }

    public void onGridNotification(GridNotification notification) {
    }

    public EnumSet<ForgeDirection> getConnectableSides() {
        return this.validSides;
    }

    public void setValidSides(EnumSet<ForgeDirection> validSides) {
        this.validSides = validSides;
        if (this.iGridNode != null) {
            this.iGridNode.updateState();
        }
    }

    public IGridNode getNode() {
        if ((this.iGridNode == null) && (Platform.isServer()) && (this.isReady)) {
            this.iGridNode = AEApi.instance().createGridNode(this);
            readFromNBT(this.data);
            this.iGridNode.updateState();
        }
        return this.iGridNode;
    }

    public void validate() {
        if ((this.iGridProxyable instanceof TileEntityAEBase)) {
            TickHandler.instance.addInit((TileEntityAEBase) this.iGridProxyable);
        }
    }

    public void onChunkUnload() {
        this.isReady = false;
        invalidate();
    }

    public void invalidate() {
        this.isReady = false;
        if (this.iGridNode != null) {
            this.iGridNode.destroy();
            this.iGridNode = null;
        }
    }

    public void onReady() {
        this.isReady = true;
        getNode();
    }

    public IGridHost getMachine() {
        return this.iGridProxyable;
    }

    public double getIdlePowerUsage() {
        return this.idleDraw;
    }

    public void setIdlePowerUsage(double idle) {
        this.idleDraw = idle;
        if (this.iGridNode != null) {
            try {
                IGrid iGrid = getGrid();
                iGrid.postEvent(new MENetworkPowerIdleChange(this.iGridNode));
            } catch (Exception e) {
            }
        }
    }

    public boolean isReady() {
        return this.isReady;
    }

    public boolean isActive() {
        if (this.iGridNode == null) {
            LogHelper.debug("AENetworkHelper-iGridNode = null");
            return false;
        }
        return this.iGridNode.isActive();
    }

    public boolean isPowered() {
        try {
            return getEnergy().isNetworkPowered();
        } catch (Exception e) {
        }
        return false;
    }

    public void gridChanged() {
        this.iGridProxyable.gridChanged();
    }

    public EnumSet<GridFlags> getFlags() {
        return this.gridFlags;
    }

    @Override
    public boolean isWorldAccessible() {
        return this.worldNode;
    }

    public void setFlags(GridFlags... requireChannel) {
        EnumSet<GridFlags> flags = EnumSet.noneOf(GridFlags.class);
        Collections.addAll(flags, requireChannel);
        this.gridFlags = flags;
    }



    public IGrid getGrid() throws Exception {
        if (this.iGridNode == null) {
            throw new Exception();
        }
        IGrid iGrid = this.iGridNode.getGrid();
        if (iGrid == null) {
            throw new Exception();
        }
        return iGrid;
    }

    public IEnergyGrid getEnergy() throws Exception {
        IGrid iGrid = getGrid();
        if (iGrid == null) {
            throw new Exception();
        }
        IEnergyGrid iEnergyGrid = iGrid.getCache(IEnergyGrid.class);
        if (iEnergyGrid == null) {
            throw new Exception();
        }
        return iEnergyGrid;
    }

    public IPathingGrid getPath() throws Exception {
        IGrid iGrid = getGrid();
        if (iGrid == null) {
            throw new Exception();
        }
        IPathingGrid iPathingGrid = iGrid.getCache(IPathingGrid.class);
        if (iPathingGrid == null) {
            throw new Exception();
        }
        return iPathingGrid;
    }

    public ITickManager getTick() throws Exception {
        IGrid iGrid = getGrid();
        if (iGrid == null) {
            throw new Exception();
        }
        ITickManager iTickManager = (ITickManager) iGrid.getCache(ITickManager.class);
        if (iTickManager == null) {
            throw new Exception();
        }
        return iTickManager;
    }

    public IStorageGrid getStorage() throws Exception {
        IGrid iGrid = getGrid();
        if (iGrid == null) {
            throw new Exception();
        }
        IStorageGrid iStorageGrid = iGrid.getCache(IStorageGrid.class);
        if (iStorageGrid == null) {
            throw new Exception();
        }
        return iStorageGrid;
    }

    public ISecurityGrid getSecurity() throws Exception {
        IGrid iGrid = getGrid();
        if (iGrid == null) {
            throw new Exception();
        }
        ISecurityGrid iSecurityGrid = iGrid.getCache(ISecurityGrid.class);
        if (iSecurityGrid == null) {
            throw new Exception();
        }
        return iSecurityGrid;
    }
}

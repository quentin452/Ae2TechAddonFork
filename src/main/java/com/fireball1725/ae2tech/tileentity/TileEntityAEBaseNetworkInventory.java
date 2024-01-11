package com.fireball1725.ae2tech.tileentity;

import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import com.fireball1725.ae2tech.events.TileEventHandler;
import com.fireball1725.ae2tech.helpers.AENetworkHelper;
import com.fireball1725.ae2tech.helpers.IGridProxyable;
import com.fireball1725.ae2tech.util.LogHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileEntityAEBaseNetworkInventory extends TileEntityAEBaseInventory implements IActionHost, IGridProxyable {
    protected AENetworkHelper gridProxy = new AENetworkHelper(this, "proxy", getItemFromTile(this), true);

    public TileEntityAEBaseNetworkInventory() {
        addNewHandler(new NetworkInventoryTileHandler());
    }

    public AENetworkHelper getProxy() {
        return this.gridProxy;
    }

    public IGridNode getGridNode(ForgeDirection dir) {
        return this.gridProxy.getNode();
    }

    public void onReady() {
        super.onReady();
        this.gridProxy.onReady();
    }

    public void onChunkUnload() {
        super.onChunkUnload();
        this.gridProxy.onChunkUnload();
    }

    public void validate() {
        super.validate();
        this.gridProxy.validate();
    }

    public void invalidate() {
        super.invalidate();
        this.gridProxy.invalidate();
    }

    public void gridChanged() {
    }

    public IGridNode getActionableNode() {
        return this.gridProxy.getNode();
    }

    class NetworkInventoryTileHandler extends TileEventHandler {
        public NetworkInventoryTileHandler() {
            super();
        }

        public void readFromNBT(NBTTagCompound data) {
            LogHelper.debug("TileEntityAEBaseNetworkInventory-Read from NBT");
            TileEntityAEBaseNetworkInventory.this.gridProxy.readFromNBT(data);
        }

        public void writeToNBT(NBTTagCompound data) {
            LogHelper.debug("TileEntityAEBaseNetworkInventory-Write to NBT");
            TileEntityAEBaseNetworkInventory.this.gridProxy.writeToNBT(data);
        }
    }
}

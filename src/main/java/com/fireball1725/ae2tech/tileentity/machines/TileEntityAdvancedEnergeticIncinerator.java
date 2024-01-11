package com.fireball1725.ae2tech.tileentity.machines;

import appeng.api.AEApi;
import appeng.api.networking.*;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.DimensionalCoord;
import appeng.api.util.IOrientable;
import com.fireball1725.ae2tech.blocks.Blocks;
import com.fireball1725.ae2tech.util.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;

public class TileEntityAdvancedEnergeticIncinerator extends TileEntity implements ISidedInventory, IGridHost, IOrientable, IGridBlock {

    protected IGridNode iGridNode = null;
    protected IGrid iGrid;
    protected int channelsInUse;
    protected boolean isActive;
    protected boolean isWorking = false;
    protected int machineState = 0;
    protected ItemStack[] itemStacks = new ItemStack[16];
    protected boolean requireChannel = false;

    public TileEntityAdvancedEnergeticIncinerator() {
        super();
    }

    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);

        if (iGridNode == null) {
            createAELink();
        }

        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
    }

    public void createAELink() {
        iGridNode = AEApi.instance().createGridNode(this);
        iGridNode.updateState();
    }

    public void destoryAELink() {
        iGridNode.destroy();
    }

    public int getState() {
        //machineState = isActive == false ? 0 : isWorking == false ? 1 : 2;
        return machineState;
    }

    public void updateMachineState() {
        machineState = isActive == false ? 0 : isWorking == false ? 1 : 2;

        if (worldObj.provider.hasNoSky) {
            worldObj.updateLightByType(EnumSkyBlock.Sky, xCoord, yCoord, zCoord);
        }

        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void updateEntity() {
        updateMachineState();
        if (iGridNode != null) {
            boolean oldActive = isActive;
            this.isActive = iGridNode.isActive();
            updateMachineState();
            if (oldActive != isActive) {
                updateMachineState();
            }
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
        readFromNBT(packet.func_148857_g());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        isActive = tag.getBoolean("isActive");
        isWorking = tag.getBoolean("isWorking");
        requireChannel = tag.getBoolean("requireChannel");
        NBTTagList nbtTagList = tag.getTagList("itemStacks", 10);
        this.itemStacks = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < nbtTagList.tagCount(); ++i) {
            NBTTagCompound nbtTagCompound = nbtTagList.getCompoundTagAt(i);
            int j = nbtTagCompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.itemStacks.length) {
                ItemStack itemStack = ItemStack.loadItemStackFromNBT(nbtTagCompound);
                this.itemStacks[j] = itemStack;
            }
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setBoolean("isActive", isActive);
        tag.setBoolean("isWorking", isWorking);
        tag.setBoolean("requireChannel", requireChannel);
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < this.itemStacks.length; ++i) {
            if (this.itemStacks[i] != null) {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte) i);
                this.itemStacks[i].writeToNBT(nbtTagCompound);
                nbtTagList.appendTag(nbtTagCompound);
            }
        }
        tag.setTag("itemStacks", nbtTagList);

    }

    @Override
    public double getIdlePowerUsage() {
        return 1; // TODO: Config Object
    }

    @Override
    public EnumSet<GridFlags> getFlags() {
        return EnumSet.of(GridFlags.REQUIRE_CHANNEL);
    }

    @Override
    public boolean isWorldAccessible() {
        return true;
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }

    @Override
    public AEColor getGridColor() {
        return AEColor.Transparent;
    }

    @Override
    public void onGridNotification(GridNotification notification) {
        LogHelper.debug("onGridNotification");
    }

    @Override
    public void setNetworkStatus(IGrid grid, int channelsInUse) {
        iGrid = grid;
        this.channelsInUse = channelsInUse;
    }

    @Override
    public EnumSet<ForgeDirection> getConnectableSides() {
        return EnumSet.of(ForgeDirection.DOWN, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.UP, ForgeDirection.WEST);
    }

    @Override
    public IGridHost getMachine() {
        return this;
    }

    @Override
    public void gridChanged() {
        LogHelper.debug("gridChanged");
    }

    @Override
    public ItemStack getMachineRepresentation() {
        ItemStack itemStack = new ItemStack(Blocks.MACHINE_ENERGETICINCINERATOR.block);
        return itemStack;
    }

    @Override
    public IGridNode getGridNode(ForgeDirection dir) {
        return iGridNode;
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection dir) {
        return AECableType.SMART;
    }

    @Override
    public void securityBreak() {
        LogHelper.debug("securityBreak");
    }

    @Override
    public int getSizeInventory() {
        return 16;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return itemStacks[var1];
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2) {
        if (this.itemStacks[var1] != null) {
            ItemStack itemStack;
            if (this.itemStacks[var1].stackSize <= var2) {
                itemStack = this.itemStacks[var1];
                this.itemStacks[var1] = null;
                //TODO: Do Inv Change
                return itemStack;
            } else {
                itemStack = this.itemStacks[var1].splitStack(var2);
                if (this.itemStacks[var1].stackSize == 0) {
                    this.itemStacks[var1] = null;
                }
                //TODO: Do Inv Change
                return itemStack;
            }
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        if (this.itemStacks[var1] != null) {
            return itemStacks[var1];
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {


        this.itemStacks[var1] = var2;
        // TODO: Update GUI
    }


    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {
        return false;
    }

    @Override
    public boolean canBeRotated() {
        return true;
    }

    @Override
    public ForgeDirection getForward() {
        return null;
    }

    @Override
    public ForgeDirection getUp() {
        return null;
    }

    @Override
    public void setOrientation(ForgeDirection Forward, ForgeDirection Up) {

    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        return false;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return false;
    }
}

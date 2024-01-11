package com.fireball1725.ae2tech.tileentity;

import appeng.api.util.ICommonTile;
import appeng.api.util.IOrientable;
import com.fireball1725.ae2tech.events.TileEventHandler;
import com.fireball1725.ae2tech.events.TileEventType;
import com.fireball1725.ae2tech.helpers.ICustomNameObject;
import com.fireball1725.ae2tech.util.ItemStackSrc;
import com.fireball1725.ae2tech.util.LogHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

public class TileEntityAEBase extends TileEntity implements IOrientable, ICommonTile, ICustomNameObject {
    public static final HashMap<Class, ItemStackSrc> myItem = new HashMap();
    private final EnumMap<TileEventType, List<TileEventHandler>> handlers = new EnumMap(TileEventType.class);
    public String customName;
    public int renderedFragment = 0;
    public boolean dropItems = true;
    private ForgeDirection forward = ForgeDirection.UNKNOWN;
    private ForgeDirection up = ForgeDirection.UNKNOWN;

    public static void registerTileItem(Class c, ItemStackSrc wat) {
        myItem.put(c, wat);
    }

    public Packet getDescriptionPacket() {
        NBTTagCompound data = new NBTTagCompound();

//        ByteBuf stream = Unpooled.buffer();
//        try {
//            writeToStream(stream);
//            if (stream.readableBytes() == 0) {
//                return null;
//            }
//        } catch (Throwable t) {
//            LogHelper.error(t);
//        }
//        data.setByteArray("X", stream.array());
        writeToNBT(data);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, data);
    }

    public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity s35PacketUpdateTileEntity) {
//        if (s35PacketUpdateTileEntity.func_148853_f() == 64) {
//            ByteBuf stream = Unpooled.copiedBuffer(s35PacketUpdateTileEntity.func_148857_g().getByteArray("X"));
//            if (readfromStream(stream)) {
//                markForUpdate();
//            }
//        }
        readFromNBT(s35PacketUpdateTileEntity.func_148857_g());
        worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
    }

    public void markForUpdate() {
        if (this.renderedFragment > 0) {
            this.renderedFragment |= 0x1;
        } else if (this.worldObj != null) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    protected ItemStack getItemFromTile(Object object) {
        ItemStackSrc itemStackSrc = (ItemStackSrc) myItem.get(object.getClass());
        if (itemStackSrc == null) {
            return null;
        }
        return itemStackSrc.stack(1);
    }

    public TileEntity getTile() {
        return this;
    }

    @Override
    public String getCustomName() {
        return hasCustomName() ? this.customName : getClass().getSimpleName();
    }

    @Override
    public boolean hasCustomName() {
        return (this.customName != null) && (this.customName.length() > 0);
    }

    public void setName(String name) {
        this.customName = name;
    }

    @Override
    public boolean canBeRotated() {
        return true;
    }

    @Override
    public ForgeDirection getForward() {
        return this.forward;
    }

    @Override
    public ForgeDirection getUp() {
        return this.up;
    }

    @Override
    public void setOrientation(ForgeDirection Forward, ForgeDirection Up) {
        this.forward = Forward;
        this.up = Up;
        markForUpdate();
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, Blocks.air);
    }

    public void onRedstoneEvent() {
    }

    protected boolean hasHandlerFor(TileEventType type) {
        List<TileEventHandler> list = (List) this.handlers.get(type);
        return list != null;
    }

    protected List<TileEventHandler> getHandlerListFor(TileEventType type) {
        List<TileEventHandler> list = (List) this.handlers.get(type);
        if (list == null) {
            this.handlers.put(type, list = new LinkedList());
        }
        return list;
    }

    protected void addNewHandler(TileEventHandler handler) {
        EnumSet<TileEventType> types = handler.getSubscribedEvents();
        for (TileEventType type : types) {
            getHandlerListFor(type).add(handler);
        }
    }

    @Override
    public final boolean canUpdate() {
        return hasHandlerFor(TileEventType.TICK);
    }

    @Override
    public final void updateEntity() {
        for (TileEventHandler handler : getHandlerListFor(TileEventType.TICK)) {
            handler.Tick();
        }
    }

    @Override
    public void onChunkUnload() {
        if (!isInvalid()) {
            invalidate();
        }
    }

    public void onChunkLoad() {
        if (isInvalid()) {
            validate();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        if (canBeRotated()) {
            nbtTagCompound.setString("orientation_forward", this.forward.name());
            nbtTagCompound.setString("orientation_up", this.up.name());
        }
        if (this.customName != null) {
            nbtTagCompound.setString("customName", this.customName);
        }
        for (TileEventHandler handler : getHandlerListFor(TileEventType.WORLD_NBT)) {
            handler.writeToNBT(nbtTagCompound);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        if (nbtTagCompound.hasKey("customName")) {
            this.customName = nbtTagCompound.getString("customName");
        } else {
            this.customName = null;
        }
        try {
            if (canBeRotated()) {
                this.forward = ForgeDirection.valueOf(nbtTagCompound.getString("orientation_forward"));
                this.up = ForgeDirection.valueOf(nbtTagCompound.getString("orientation_up"));
            }
        } catch (IllegalArgumentException e) {
        }
        for (TileEventHandler handler : getHandlerListFor(TileEventType.WORLD_NBT)) {
            handler.readFromNBT(nbtTagCompound);
        }
    }

    public final void writeToStream(ByteBuf data) {
        LogHelper.debug("writeToStream");
        try {
            if (canBeRotated()) {
                byte orientation = (byte) (this.up.ordinal() << 3 | this.forward.ordinal());
                data.writeByte(orientation);
            }
            for (TileEventHandler handler : getHandlerListFor(TileEventType.NETWORK)) {
                handler.writeToStream(data);
            }
        } catch (Throwable t) {
            LogHelper.error(t);
        }
    }

    public final boolean readfromStream(ByteBuf data) {
        LogHelper.debug("readFromStream");
        boolean output = false;
        try {
            if (canBeRotated()) {
                ForgeDirection old_Forward = this.forward;
                ForgeDirection old_Up = this.up;

                byte orientation = data.readByte();
                this.forward = ForgeDirection.getOrientation(orientation & 0x7);
                this.up = ForgeDirection.getOrientation(orientation >> 3);

                output = (!this.forward.equals(old_Forward)) || (!this.up.equals(old_Up));
            }
            this.renderedFragment = 100;
            for (TileEventHandler handler : getHandlerListFor(TileEventType.NETWORK)) {
                if (handler.readFromStream(data)) {
                    output = true;
                }
            }
            if ((this.renderedFragment & 0x1) == 1) {
                output = true;
            }
        } catch (Throwable t) {
            LogHelper.error(t);
        }
        return output;
    }

    public void onPlacement(ItemStack itemStack, EntityPlayer entityPlayer, int side) {
        if (itemStack.hasTagCompound()) {
            //TODO: Restore Settings from item...
        }
    }

    public void onReady() {
    }

    public void securityBreak() {
        this.worldObj.func_147480_a(this.xCoord, this.yCoord, this.zCoord, true);
        this.dropItems = false;
    }

    public void saveChanges() {
        super.markDirty();
    }

    public boolean requiresTESR() {
        return false;
    }

    @Override
    public void getDrops(World world, int i, int i1, int i2, List<ItemStack> list) {
        if ((this instanceof IInventory)) {
            IInventory inventory = (IInventory) this;
            for (int l = 0; l < inventory.getSizeInventory(); l++) {
                ItemStack itemStack = inventory.getStackInSlot(l);
                if (itemStack != null) {
                    list.add(itemStack);
                }
            }
        }
    }
}

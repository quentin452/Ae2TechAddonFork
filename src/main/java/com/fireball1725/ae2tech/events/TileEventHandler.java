package com.fireball1725.ae2tech.events;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;
import java.util.EnumSet;

public abstract class TileEventHandler {
    final EnumSet<TileEventType> supportedEvents;

    public TileEventHandler(TileEventType... events) {
        this.supportedEvents = EnumSet.noneOf(TileEventType.class);
        for (TileEventType t : events) {
            this.supportedEvents.add(t);
        }
    }

    public EnumSet<TileEventType> getSubscribedEvents() {
        return this.supportedEvents;
    }

    public void Tick() {
    }

    public void writeToNBT(NBTTagCompound data) {
    }

    public void readFromNBT(NBTTagCompound data) {
    }

    public void writeToStream(ByteBuf data) throws IOException {
    }

    @SideOnly(Side.CLIENT)
    public boolean readFromStream(ByteBuf data) throws IOException {
        return false;
    }
}

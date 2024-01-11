package com.fireball1725.ae2tech.events;

import com.fireball1725.ae2tech.tileentity.TileEntityAEBase;
import com.fireball1725.ae2tech.util.LogHelper;
import com.fireball1725.ae2tech.util.Platform;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;

public class TickHandler {
    public static final TickHandler instance = new TickHandler();
    private final Queue<Callable> callQueue = new LinkedList();
    private final HandlerRep server = new HandlerRep();
    private final HandlerRep client = new HandlerRep();

    HandlerRep getRepo() {
        if (Platform.isServer()) {
            return this.server;
        }
        return this.client;
    }

    public void addInit(TileEntityAEBase tile) {
        if (Platform.isServer()) {
            getRepo().tiles.add(tile);
        }
    }

    public void shutdown() {
        getRepo().clear();
    }

    @SubscribeEvent
    public void unloadWorld(WorldEvent.Unload ev) {

    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load load) {
        for (Object te : load.getChunk().chunkTileEntityMap.values()) {
            if ((te instanceof TileEntityAEBase)) {
                ((TileEntityAEBase) te).onChunkLoad();
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if ((event.type == TickEvent.Type.SERVER) && (event.phase == TickEvent.Phase.END)) {
            HandlerRep repo = getRepo();
            while (!repo.tiles.isEmpty()) {
                LogHelper.debug("TileEntityAEBase found, marking as ready...");
                TileEntityAEBase tileEntityAEBase = (TileEntityAEBase) repo.tiles.poll();
                tileEntityAEBase.onReady();
            }
            Callable c = null;
            while ((c = (Callable) this.callQueue.poll()) != null) {
                try {
                    c.call();
                } catch (Exception e) {
                    LogHelper.error(e);
                }
            }
        }
    }

    class HandlerRep {
        public Queue<TileEntityAEBase> tiles = new LinkedList();

        HandlerRep() {
        }

        public void clear() {
            this.tiles = new LinkedList();
        }
    }
}

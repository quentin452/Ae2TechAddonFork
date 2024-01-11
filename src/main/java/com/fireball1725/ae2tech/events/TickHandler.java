package com.fireball1725.ae2tech.events;

import com.fireball1725.ae2tech.tileentity.TileEntityAEBase;
import com.fireball1725.ae2tech.util.LogHelper;
import com.fireball1725.ae2tech.util.Platform;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TickHandler {
    public static final TickHandler instance = new TickHandler();
    private final Queue<Callable> callQueue = new LinkedList<>();
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
        if (event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.END) {
            HandlerRep repo = getRepo();

            if (!repo.tiles.isEmpty()) {
                LogHelper.debug("Processing TileEntityAEBase batch...");

                List<TileEntityAEBase> batch = new ArrayList<>();
                repo.tiles.drainTo(batch);

                for (TileEntityAEBase tileEntityAEBase : batch) {
                    tileEntityAEBase.onReady();
                }
            }

            Callable c;
            while ((c = this.callQueue.poll()) != null) {
                try {
                    c.call();
                } catch (Exception e) {
                    LogHelper.error(e);
                }
            }
        }
    }


    class HandlerRep {
        public BlockingQueue<TileEntityAEBase> tiles = new LinkedBlockingQueue<>();

        HandlerRep() {
        }

        public void clear() {
            this.tiles.clear();
        }
    }
}

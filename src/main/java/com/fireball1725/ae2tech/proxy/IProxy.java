package com.fireball1725.ae2tech.proxy;

import java.io.File;

public interface IProxy {
    // Register Blocks
    public abstract void registerBlocks();

    // Register TileEntities
    public abstract void registerTileEntities();

    // Register Entites
    //public abstract void registerEntities();

    // Register Events
    public abstract void registerEvents();

    // Register Items
    public abstract void registerItems();

    // Register PlayerEvents
    //public abstract void registerPlayerEvents();

    // Register Packets
    //public abstract void initPacketHandler();

    //public abstract void postInitPacketHandler();

    // Register Recipes
    public abstract void registerRecipes(File configFolder);
}

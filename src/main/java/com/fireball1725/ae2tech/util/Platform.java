package com.fireball1725.ae2tech.util;

import cpw.mods.fml.common.FMLCommonHandler;

public class Platform {
    public static boolean isClient() {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }

    public static boolean isServer() {
        return FMLCommonHandler.instance().getEffectiveSide().isServer();
    }
}

package com.fireball1725.ae2tech;

import com.fireball1725.ae2tech.handler.ConfigurationHandler;
import com.fireball1725.ae2tech.lib.Autoupdate;
import com.fireball1725.ae2tech.lib.Reference;
import com.fireball1725.ae2tech.proxy.IProxy;
import com.fireball1725.ae2tech.reference.Settings;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

import java.io.File;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, dependencies = Reference.DEPENDENCIES, version = Reference.VERSION_BUILD)
public class AE2Tech {
    @Mod.Instance
    public static AE2Tech instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;
    public static String LogDebugString = "@DEBUG@";
    public static boolean LogDebug = false;
    private static File configFolder;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());

        //if(!LogDebugString.equals("@DEBUG@")) {
        //	LogDebug = false;
        //}
        if (Settings.SETTINGS_DEBUG_FORCEDEBUG) {
            LogDebug = true;
        }

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandler());

        proxy.registerBlocks();
        proxy.registerItems();

        proxy.registerEvents();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerRecipes(configFolder);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Settings.SETTINGS_UPDATES_DISABLECHECK) {
            if (Autoupdate.CheckForUpdates()) {
                FMLInterModComms.sendRuntimeMessage(this, "VersionChecker", "addUpdate", Autoupdate.GetUpdateDetails());
            }
        }
    }
}

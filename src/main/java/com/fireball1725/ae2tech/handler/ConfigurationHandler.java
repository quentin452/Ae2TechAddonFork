package com.fireball1725.ae2tech.handler;

import com.fireball1725.ae2tech.helpers.ConfigurationHelper;
import com.fireball1725.ae2tech.lib.Reference;
import com.fireball1725.ae2tech.reference.Settings;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigurationHandler {
    public static Configuration configuration;

    public static void init(File configFile) {
        if (configuration == null) {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
    }

    public static void loadConfiguration() {
        Settings.MACHINE_INCINERATOR_POWER_IDLE = ConfigurationHelper.getInt(configuration, "Energetic Incinerator Idle Power Draw", "Machines", 2, "This sets the idle power the machine uses");
        Settings.PERFORMANCE_UPGRADE_POWER_MULTIPLIER = ConfigurationHelper.getInt(configuration, "Performance Upgrade Card Power Multiplier", "Upgrades", 2, "This sets how much to multiply the power usage by");

        Settings.SETTINGS_UPDATES_DISABLECHECK = ConfigurationHelper.getBoolean(configuration, "Check for Updates", "Updates", true, "This setting asks the update server if there are any updates, turn this to false if you don't want to automatically check for updates");

        Settings.SETTINGS_DEBUG_FORCEDEBUG = ConfigurationHelper.getBoolean(configuration, "Force Debug Mode", "Debug", false, "This will force debug mode to be on, basically your log will fill up with a bunch of stuff");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(Reference.MOD_ID)) {
            loadConfiguration();
        }
    }
}

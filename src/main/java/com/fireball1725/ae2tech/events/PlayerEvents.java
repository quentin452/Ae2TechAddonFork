package com.fireball1725.ae2tech.events;

import com.fireball1725.ae2tech.AE2Tech;
import com.fireball1725.ae2tech.lib.Autoupdate;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class PlayerEvents {
    @SubscribeEvent
    public void onPlayerConnects(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayer) {
            if (!event.world.isRemote) {
                EntityPlayer player = (EntityPlayer) event.entity;

                if (isOp(player)) {
                    if (Autoupdate.CheckForUpdates()) {
                        String updateVersion = Autoupdate.GetUpdateVersion();

                        if (updateVersion != "") {
                            player.addChatComponentMessage(
                                    new ChatComponentText("Build #" + updateVersion + " of AE2 Tech Add-On is available")
                            );
                        }
                    }
                }

                if (AE2Tech.LogDebug) {
                    player.addChatComponentMessage(
                            new ChatComponentText("§cAE2 Tech Add-On is running in Debug Mode")
                    );
                }
            }
        }
    }

    private boolean isOp(String playerName) {
        //return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(playerName);
        return true;
    }

    private boolean isOp(EntityPlayer player) {
        return isOp(player.getDisplayName());
    }
}

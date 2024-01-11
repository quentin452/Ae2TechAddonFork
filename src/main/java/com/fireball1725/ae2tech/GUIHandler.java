package com.fireball1725.ae2tech;

import com.fireball1725.ae2tech.container.machines.ContainerAdvancedEnergeticIncinerator;
import com.fireball1725.ae2tech.container.machines.ContainerEnergeticCrumbler;
import com.fireball1725.ae2tech.container.machines.ContainerEnergeticIncinerator;
import com.fireball1725.ae2tech.gui.machines.GuiAdvancedEnergeticIncinerator;
import com.fireball1725.ae2tech.gui.machines.GuiEnergeticCrumbler;
import com.fireball1725.ae2tech.gui.machines.GuiEnergeticIncinerator;
import com.fireball1725.ae2tech.tileentity.machines.TileEntityAdvancedEnergeticIncinerator;
import com.fireball1725.ae2tech.tileentity.machines.TileEntityEnergeticCrumbler;
import com.fireball1725.ae2tech.tileentity.machines.TileEntityEnergeticIncinerator;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GUIHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null) {
            switch (ID) {
                case 0: // Energetic Incinerator GUI
                    return new ContainerEnergeticIncinerator(player.inventory, (TileEntityEnergeticIncinerator) tileEntity);
                case 1: // Advanced Energetic Incinerator GUI
                    return new ContainerAdvancedEnergeticIncinerator(player.inventory, (TileEntityAdvancedEnergeticIncinerator) tileEntity);
                case 2: // Energetic Crumbler GUI
                    return new ContainerEnergeticCrumbler(player.inventory, (TileEntityEnergeticCrumbler) tileEntity);
                default:
                    return false;
            }
        }

        return false;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null) {
            switch (ID) {
                case 0: // Energetic Incinerator GUI
                    return new GuiEnergeticIncinerator(player.inventory, (TileEntityEnergeticIncinerator) tileEntity);
                case 1: // Advanced Energetic Incinerator GUI
                    return new GuiAdvancedEnergeticIncinerator(player.inventory, (TileEntityAdvancedEnergeticIncinerator) tileEntity);
                case 2: // Energetic Crumbler GUI
                    return new GuiEnergeticCrumbler(player.inventory, (TileEntityEnergeticCrumbler) tileEntity);
                default:
                    return false;
            }
        }

        return false;
    }
}

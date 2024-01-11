package com.fireball1725.ae2tech.tileentity;

import com.fireball1725.ae2tech.helpers.Vector3n;
import com.fireball1725.ae2tech.lib.Reference;
import com.fireball1725.ae2tech.tileentity.machines.TileEntityAdvancedEnergeticIncinerator;
import com.fireball1725.ae2tech.tileentity.machines.TileEntityEnergeticCrumbler;
import com.fireball1725.ae2tech.tileentity.machines.TileEntityEnergeticIncinerator;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public enum Tiles {
    MACHINE_ENERGETICINCINERATOR(TileEntityEnergeticIncinerator.class, "energeticincinerator"),
    MACHINE_ADVANCEDENERGETICINCINERATOR(TileEntityAdvancedEnergeticIncinerator.class, "advancedenergeticincinerator"),

    MACHINE_ENERGETICCRUMBER(TileEntityEnergeticCrumbler.class, "energeticcrumbler"),;

    public final Class<? extends TileEntity> tileClass;
    private final String ID;

    Tiles(Class<? extends TileEntity> clazz, String identifier) {
        tileClass = clazz;
        ID = identifier;
    }

    public static void registerAll() {
        for (Tiles t : Tiles.values())
            t.register();
    }

    public static <T extends TileEntity> T getTileEntity(IBlockAccess access, Vector3n loc) {
        return Tiles.<T>getTileEntity(access, loc.x, loc.y, loc.z);
    }

    @SuppressWarnings("unchecked")
    public static <T extends TileEntity> T getTileEntity(IBlockAccess access, int x, int y, int z) {
        TileEntity te = access.getTileEntity(x, y, z);
        try {
            return (T) te;
        } catch (ClassCastException e) {
            return null;
        }
    }

    private void register() {
        GameRegistry.registerTileEntity(tileClass, "tile." + Reference.MOD_ID + "." + ID.toLowerCase());
    }
}

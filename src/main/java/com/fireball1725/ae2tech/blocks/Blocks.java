package com.fireball1725.ae2tech.blocks;

import appeng.api.config.Upgrades;
import com.fireball1725.ae2tech.blocks.machines.BlockEnergeticIncinerator;
import com.fireball1725.ae2tech.creativetab.ModCreativeTabs;
import com.fireball1725.ae2tech.lib.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public enum Blocks {
    MACHINE_ENERGETICINCINERATOR("machine.energeticincinerator", new BlockEnergeticIncinerator(), ModCreativeTabs.Machines),
    //MACHINE_ADVANCEDENERGETICINCINERATOR("machine.advancedenergeticincinerator", new BlockAdvancedEnergeticIncinerator(), ModCreativeTabs.Machines),

    //MACHINE_ENERGETICCRUMBLER("machine.enetgeticcrumbler", new BlockEnergeticCrumbler(), ModCreativeTabs.Machines),

    ;
    private static boolean registered = false;
    public final Block block;
    private final String internalName;
    private final Class<? extends ItemBlock> itemBlockClass;
    private final CreativeTabs creativeTabs;

    Blocks(String internalName, Block block) {
        this(internalName, block, ItemBlock.class, null);
    }

    Blocks(String internalName, Block block, CreativeTabs creativeTabs) {
        this(internalName, block, ItemBlock.class, creativeTabs);
    }

    Blocks(String internalName, Block block, Class<? extends ItemBlock> itemBlockClass) {
        this(internalName, block, itemBlockClass, null);
    }

    Blocks(String internalName, Block block, Class<? extends ItemBlock> itemBlockClass, CreativeTabs creativeTabs) {
        this.internalName = internalName;
        this.block = block;
        this.itemBlockClass = itemBlockClass;
        this.creativeTabs = creativeTabs;
        block.setBlockName(Reference.MOD_ID + "." + internalName);
    }

    public static void registerAll() {
        if (registered)
            return;
        for (Blocks b : Blocks.values())
            b.register();
        registered = true;

        Upgrades.REDSTONE.registerItem(new ItemStack(Blocks.MACHINE_ENERGETICINCINERATOR.block, 1), 1);
    }

    public String getInternalName() {
        return internalName;
    }

    public String getStatName() {
        return StatCollector.translateToLocal(block.getUnlocalizedName().replace("tile.", "block."));
    }

    private void register() {
        GameRegistry.registerBlock(block.setCreativeTab(creativeTabs).setBlockTextureName(Reference.MOD_ID + ":" + internalName), itemBlockClass, "tile." + internalName);
    }
}

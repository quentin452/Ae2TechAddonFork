package com.fireball1725.ae2tech.creativetab;

import com.fireball1725.ae2tech.blocks.Blocks;
import com.fireball1725.ae2tech.lib.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModCreativeTabs {
    public static final CreativeTabs Machines = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public Item getTabIconItem() {
            return new ItemStack(Blocks.MACHINE_ENERGETICINCINERATOR.block, 0).getItem();
        }

        @Override
        @SideOnly(Side.CLIENT)
        public String getTranslatedTabLabel() {
            return "Applied Energistics 2 - Machines";
        }
    };
}

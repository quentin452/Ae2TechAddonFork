package com.fireball1725.ae2tech.items;

import com.fireball1725.ae2tech.creativetab.ModCreativeTabs;
import com.fireball1725.ae2tech.items.upgradecards.ItemMachinePerformanceUpgrade;
import com.fireball1725.ae2tech.lib.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public enum Items {
    CARD_MACHINEPERFUPGRADE("card.machineperfupgrade", new ItemMachinePerformanceUpgrade(), ModCreativeTabs.Machines),;
    public final Item item;
    private final String internalName;

    Items(String internalName, Item item, CreativeTabs creativeTabs) {
        this.internalName = internalName;
        this.item = item.setTextureName(Reference.MOD_ID + ":" + internalName);
        item.setUnlocalizedName(Reference.MOD_ID + "." + internalName);
        item.setCreativeTab(creativeTabs);
    }

    public static void registerAll() {
        for (Items i : Items.values())
            i.register();
    }

    private void register() {
        GameRegistry.registerItem(item, internalName);
    }

    public String getInternalName() {
        return internalName;
    }

    public String getStatName() {
        return StatCollector.translateToLocal(item.getUnlocalizedName());
    }

    public ItemStack getStack(int damage, int size) {
        return new ItemStack(item, size, damage);
    }
}

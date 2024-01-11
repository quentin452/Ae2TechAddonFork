package com.fireball1725.ae2tech.items.upgradecards;

import appeng.api.config.Upgrades;
import appeng.api.implementations.items.IUpgradeModule;
import com.fireball1725.ae2tech.reference.Names;
import com.fireball1725.ae2tech.reference.Textures;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

import java.util.List;

public class ItemMachinePerformanceUpgrade extends Item implements IUpgradeModule {
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public ItemMachinePerformanceUpgrade() {
        super();
        this.setMaxStackSize(64);
        this.setHasSubtypes(true);
        this.setUnlocalizedName(Names.Items.PERFORMANCE_UPGRADE);
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("item.%s%s", Textures.RESOURCE_PREFIX, Names.Items.PERFORMANCE_UPGRADE);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s%s.%s", Textures.RESOURCE_PREFIX, Names.Items.PERFORMANCE_UPGRADE, Names.Items.PERFORMANCE_UPGRADE_SUBTYPES[MathHelper.clamp_int(itemStack.getItemDamage(), 0, Names.Items.PERFORMANCE_UPGRADE_SUBTYPES.length - 1)]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
        for (int meta = 0; meta < Names.Items.PERFORMANCE_UPGRADE_SUBTYPES.length; ++meta) {
            list.add(new ItemStack(this, 1, meta));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return icons[MathHelper.clamp_int(meta, 0, Names.Items.PERFORMANCE_UPGRADE_SUBTYPES.length - 1)];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        icons = new IIcon[Names.Items.PERFORMANCE_UPGRADE_SUBTYPES.length];

        for (int i = 0; i < Names.Items.PERFORMANCE_UPGRADE_SUBTYPES.length; i++) {
            icons[i] = iconRegister.registerIcon(Textures.RESOURCE_PREFIX + Names.Items.PERFORMANCE_UPGRADE + "." + Names.Items.PERFORMANCE_UPGRADE_SUBTYPES[i]);
        }
    }

    @Override
    public Upgrades getType(ItemStack itemstack) {
        return null;
    }


}

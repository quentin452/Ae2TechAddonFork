package com.fireball1725.ae2tech.blocks.machines;

import com.fireball1725.ae2tech.AE2Tech;
import com.fireball1725.ae2tech.blocks.BlockAEBase;
import com.fireball1725.ae2tech.lib.Reference;
import com.fireball1725.ae2tech.tileentity.machines.TileEntityEnergeticIncinerator;
import com.fireball1725.ae2tech.util.LogHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockEnergeticIncinerator extends BlockAEBase {
    IIcon iconTop, iconBottom, iconSide;
    IIcon[] iconFront;
    String internalName;

    public BlockEnergeticIncinerator() {
        super(Material.iron);
        setHardness(3.5F);
        setResistance(10.0F);
        setStepSound(soundTypeMetal);
        setTileEntity(TileEntityEnergeticIncinerator.class);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityEnergeticIncinerator();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int p_149727_6_, float offsetX, float offsetY, float offsetZ) {

        TileEntityEnergeticIncinerator tileEntityEnergeticIncinerator = getTileEntity(world, x, y, z, TileEntityEnergeticIncinerator.class);
        if (tileEntityEnergeticIncinerator != null) {
            LogHelper.debug("Debug Info: ");
            LogHelper.debug("isPowered: " + tileEntityEnergeticIncinerator.isPowered());
            LogHelper.debug("isActive: " + tileEntityEnergeticIncinerator.isActive());
            LogHelper.debug("isWorking: " + tileEntityEnergeticIncinerator.isWorking());
        }

        super.onBlockActivated(world, x, y, z, entityPlayer, p_149727_6_, offsetX, offsetY, offsetZ);
        if (!world.isRemote) {
            entityPlayer.openGui(AE2Tech.instance, 0, world, x, y, z);
            return true;
        }

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.internalName = this.getUnlocalizedName().replace("tile." + Reference.MOD_ID + ".", "");

        iconFront = new IIcon[3];
        iconFront[0] = iconRegister.registerIcon(Reference.MOD_ID + ":" + internalName + "Front");
        iconFront[1] = iconRegister.registerIcon(Reference.MOD_ID + ":" + internalName + "FrontOn");
        iconFront[2] = iconRegister.registerIcon(Reference.MOD_ID + ":" + internalName + "FrontWorking");

        iconSide = iconRegister.registerIcon(Reference.MOD_ID + ":" + internalName + "Side");
        iconTop = iconRegister.registerIcon(Reference.MOD_ID + ":" + internalName);
        iconBottom = iconRegister.registerIcon(Reference.MOD_ID + ":" + internalName + "Bottom");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileEntityEnergeticIncinerator tileEntityEnergeticIncinerator = getTileEntity(world, x, y, z, TileEntityEnergeticIncinerator.class);

        int meta = world.getBlockMetadata(x, y, z);
        int state = 0;
        if (tileEntityEnergeticIncinerator != null) {
            state = tileEntityEnergeticIncinerator.getState();
        }

        return (meta == 0 && side == 4 ? this.iconFront[state] : side == 1 ? this.iconTop : (side == 0 ? this.iconBottom : (side != meta ? this.iconSide : this.iconFront[state])));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return (meta == 0 && side == 4 ? this.iconFront[0] : side == 1 ? this.iconTop : (side == 0 ? this.iconBottom : (side != meta ? this.iconSide : this.iconFront[0])));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_, Random p_149734_5_) {
        TileEntityEnergeticIncinerator tileEntityEnergeticIncinerator = getTileEntity(p_149734_1_, p_149734_2_, p_149734_3_, p_149734_4_, TileEntityEnergeticIncinerator.class);
        if (tileEntityEnergeticIncinerator.getState() == 2) {
            int l = p_149734_1_.getBlockMetadata(p_149734_2_, p_149734_3_, p_149734_4_);
            float f = (float) p_149734_2_ + 0.5F;
            float f1 = (float) p_149734_3_ + 0.0F + p_149734_5_.nextFloat() * 6.0F / 16.0F;
            float f2 = (float) p_149734_4_ + 0.5F;
            float f3 = 0.52F;
            float f4 = p_149734_5_.nextFloat() * 0.6F - 0.3F;

            if (l == 4) {
                p_149734_1_.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                p_149734_1_.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 5) {
                p_149734_1_.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                p_149734_1_.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 2) {
                p_149734_1_.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                p_149734_1_.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
            } else if (l == 3) {
                p_149734_1_.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                p_149734_1_.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        int dir = MathHelper.floor_double(entityLivingBase.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        switch (dir) {
            case 0:
                world.setBlockMetadataWithNotify(x, y, z, 2, 2);
                break;
            case 1:
                world.setBlockMetadataWithNotify(x, y, z, 5, 2);
                break;
            case 2:
                world.setBlockMetadataWithNotify(x, y, z, 3, 2);
                break;
            case 3:
                world.setBlockMetadataWithNotify(x, y, z, 4, 2);
                break;
        }
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        TileEntityEnergeticIncinerator tileEntityBase = getTileEntity(world, x, y, z, TileEntityEnergeticIncinerator.class);

        if (tileEntityBase != null) {
            switch (tileEntityBase.getState()) {
                case 0:
                    return 0;
                case 1:
                    return 12;
                case 2:
                    return 15;
            }
        }

        return 0;
    }
}

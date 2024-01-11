package com.fireball1725.ae2tech.blocks.machines;

import com.fireball1725.ae2tech.AE2Tech;
import com.fireball1725.ae2tech.blocks.Blocks;
import com.fireball1725.ae2tech.lib.Reference;
import com.fireball1725.ae2tech.tileentity.machines.TileEntityAdvancedEnergeticIncinerator;
import com.fireball1725.ae2tech.util.LogHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAdvancedEnergeticIncinerator extends BlockContainer {
    IIcon iconTop, iconBottom, iconSide;
    IIcon[] iconFront;
    String internalName;

    public BlockAdvancedEnergeticIncinerator() {
        super(Material.iron);
        setHardness(3.5F);
        setResistance(10.0F);
        setStepSound(soundTypeMetal);
    }

    public static <T> T getTileEntity(IBlockAccess access, int x, int y, int z, Class<T> clazz) {
        TileEntity te = access.getTileEntity(x, y, z);
        return !clazz.isInstance(te) ? null : (T) te;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityAdvancedEnergeticIncinerator();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_) {
        TileEntityAdvancedEnergeticIncinerator tileEntityAdvancedEnergeticIncinerator = getTileEntity(world, x, y, z, TileEntityAdvancedEnergeticIncinerator.class);

        if (tileEntityAdvancedEnergeticIncinerator != null) {
            tileEntityAdvancedEnergeticIncinerator.destoryAELink();
        }

        super.breakBlock(world, x, y, z, block, p_149749_6_);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int p_149727_6_, float offsetX, float offsetY, float offsetZ) {
        ItemStack currentItem = entityPlayer.inventory.getCurrentItem();

        //if (entityPlayer.isSneaking() && currentItem != null && currentItem == AEApi.instance().items().itemNetworkTool.stack(1)) {
        // TODO: Figure out if the player has a wrench or acceptable item to shift click the item
        if (entityPlayer.isSneaking() && currentItem != null) {
            dropBlockAsItem(world, x, y, z, getDropWithNBT(world, x, y, z));
            world.setBlockToAir(x, y, z);
            return true;
        }

        if (!world.isRemote) {
            LogHelper.debug("Open GUI?");
            entityPlayer.openGui(AE2Tech.instance, 1, world, x, y, z);
            return true;
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileEntityAdvancedEnergeticIncinerator tileEntityAdvancedEnergeticIncinerator = getTileEntity(world, x, y, z, TileEntityAdvancedEnergeticIncinerator.class);

        int meta = world.getBlockMetadata(x, y, z);
        int state = 0;
        if (tileEntityAdvancedEnergeticIncinerator != null) {
            state = tileEntityAdvancedEnergeticIncinerator.getState();
        }

        return (meta == 0 && side == 4 ? this.iconFront[state] : side == 1 ? this.iconTop : (side == 0 ? this.iconBottom : (side != meta ? this.iconSide : this.iconFront[state])));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return (meta == 0 && side == 4 ? this.iconFront[0] : side == 1 ? this.iconTop : (side == 0 ? this.iconBottom : (side != meta ? this.iconSide : this.iconFront[0])));
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
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        TileEntityAdvancedEnergeticIncinerator tileEntityAdvancedEnergeticIncinerator = getTileEntity(world, x, y, z, TileEntityAdvancedEnergeticIncinerator.class);

        if (tileEntityAdvancedEnergeticIncinerator != null) {
            switch (tileEntityAdvancedEnergeticIncinerator.getState()) {
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

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        //super.onBlockPlacedBy(world, x, y, z, entityLivingBase, itemStack);
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

    public ItemStack getDropWithNBT(World world, int x, int y, int z) {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileEntityAdvancedEnergeticIncinerator) {
            ItemStack itemStack = new ItemStack(Blocks.MACHINE_ENERGETICINCINERATOR.block, 1);

            //TODO: Write nbt data from tileentity to itemstack

            return itemStack;
        }

        return null;
    }

}

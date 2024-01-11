package com.fireball1725.ae2tech.blocks;

import com.fireball1725.ae2tech.lib.Reference;
import com.fireball1725.ae2tech.tileentity.TileEntityAEBase;
import com.fireball1725.ae2tech.util.ItemStackSrc;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockAEBase extends BlockContainer {
    @SideOnly(Side.CLIENT)
    public IIcon renderIcon;
    protected boolean isInventory = false;
    protected boolean hasSubtypes = false;
    private Class<? extends TileEntity> tileEntityType = null;

    protected BlockAEBase(Material material) {
        super(material);

        if (material == Material.glass)
            setStepSound(Block.soundTypeGlass);
        else if (material == Material.rock)
            setStepSound(Block.soundTypeStone);
        else
            setStepSound(Block.soundTypeMetal);

        setLightOpacity(15);
        setLightLevel(0.0F);
        setHardness(2.2F);
        setHarvestLevel("pickaxe", 0);
    }

    public static <T> T getTileEntity(IBlockAccess world, int x, int y, int z, Class<T> clazz) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return !clazz.isInstance(tileEntity) ? null : (T) tileEntity;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        if (hasBlockTileEntity()) {
            try {
                return (TileEntity) this.tileEntityType.newInstance();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {

    }

    public int getComparatorInputOverride(World world, int x, int y, int z, int s) {
        TileEntity tileEntity = getTileEntity(world, x, y, z);
        if (tileEntity instanceof IInventory) {
            return Container.calcRedstoneFromInventory((IInventory) tileEntity);
        }
        return 0;
    }

    protected void setTileEntity(Class<? extends TileEntity> c) {
        TileEntityAEBase.registerTileItem(c, new ItemStackSrc(this, 0));
        String tileName = "tile." + Reference.MOD_ID + "." + c.getSimpleName();

        GameRegistry.registerTileEntity(this.tileEntityType = c, tileName);
        this.isInventory = IInventory.class.isAssignableFrom(c);
        setTileProvider(hasBlockTileEntity());
    }

    private void setTileProvider(boolean b) {
        ReflectionHelper.setPrivateValue(Block.class, this, Boolean.valueOf(b), new String[]{"isTileProvider"});
    }

    private boolean hasBlockTileEntity() {
        return this.tileEntityType != null;
    }

    public Class<? extends TileEntity> getTileEntityClass() {
        return this.tileEntityType;
    }

    public <T extends TileEntity> TileEntity getTileEntity(IBlockAccess world, int x, int y, int z) {
        if (!hasBlockTileEntity()) {
            return null;
        }

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (this.tileEntityType.isInstance(tileEntity)) {
            return tileEntity;
        }

        return null;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        dropInventory(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }

    protected void dropInventory(World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (!(tileEntity instanceof IInventory))
        {
            return;
        }

        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (itemStack != null && itemStack.stackSize > 0)
            {
                Random rand = new Random();

                float dX = rand.nextFloat() * 0.8F + 0.1F;
                float dY = rand.nextFloat() * 0.8F + 0.1F;
                float dZ = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, itemStack.copy());

                if (itemStack.hasTagCompound())
                {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                itemStack.stackSize = 0;
            }
        }
    }
}

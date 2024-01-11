package com.fireball1725.ae2tech.blocks.machines;

import com.fireball1725.ae2tech.blocks.BlockAEBase;
import net.minecraft.block.material.Material;

public class BlockEnergeticCrumbler extends BlockAEBase {
    //    IIcon iconTop, iconBottom, iconSide;
//    IIcon[] iconFront;
//    String internalName;
//
    public BlockEnergeticCrumbler() {
        super(Material.iron);
        setHardness(3.5F);
        setResistance(10.0F);
        setStepSound(soundTypeMetal);
    }
//
//    @Override
//    public TileEntity createNewTileEntity(World world, int i) {
//        return new TileEntityEnergeticCrumbler();
//    }
//
//    @Override
//    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int p_149727_6_, float offsetX, float offsetY, float offsetZ) {
//        super.onBlockActivated(world, x, y, z, entityPlayer, p_149727_6_, offsetX, offsetY, offsetZ);
//        if (!world.isRemote) {
//            entityPlayer.openGui(AE2Tech.instance, 2, world, x, y, z);
//            return true;
//        }
//
//        return true;
//    }
}

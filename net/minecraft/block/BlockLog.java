package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public abstract class BlockLog extends BlockRotatedPillar
{
    public static final PropertyEnum<EnumAxis> LOG_AXIS = PropertyEnum.<EnumAxis>create("axis", EnumAxis.class);

    public BlockLog()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(2.0F);
        this.setStepSound(soundTypeWood);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        int i = 4;
        int j = 5;

        if (worldIn.isAreaLoaded(pos.add(-j, -j, -j), pos.add(j, j, j)))
            for (BlockPos blockpos : BlockPos.getAllInBox(pos.add(-i, -i, -i), pos.add(i, i, i)))
            {
                IBlockState iblockstate = worldIn.getBlockState(blockpos);

                if (iblockstate.getBlock().getMaterial() == Material.leaves && !iblockstate.getValue(BlockLeaves.CHECK_DECAY))
                    worldIn.setBlockState(blockpos, iblockstate.withProperty(BlockLeaves.CHECK_DECAY, true), 4);
            }
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
        		.withProperty(LOG_AXIS, EnumAxis.fromFacingAxis(facing.getAxis()));
    }

    public static enum EnumAxis implements IStringSerializable
    {
        X("x"),
        Y("y"),
        Z("z"),
        NONE("none");

        private final String name;

        private EnumAxis(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.name;
        }

        public static EnumAxis fromFacingAxis(EnumFacing.Axis axis)
        {
            switch (axis)
            {
                case X:
                    return X;

                case Y:
                    return Y;

                case Z:
                    return Z;

                default:
                    return NONE;
            }
        }

        public String getName()
        {
            return this.name;
        }
    }
}

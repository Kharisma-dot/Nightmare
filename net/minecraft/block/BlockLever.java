package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLever extends Block
{
    public static final PropertyEnum<EnumOrientation> FACING = PropertyEnum.<EnumOrientation>create("facing", EnumOrientation.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    protected BlockLever()
    {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumOrientation.NORTH).withProperty(POWERED, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    /**
     * Check whether this Block can be placed on the given side
     */
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return canPlaceWithOffset(worldIn, pos, side.getOpposite());
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        for (EnumFacing enumfacing : EnumFacing.values())
            if (canPlaceWithOffset(worldIn, pos, enumfacing))
                return true;

        return false;
    }

    protected static boolean canPlaceWithOffset(World world, BlockPos pos, EnumFacing facing)
    {
        return BlockButton.canPlaceWithOffset(world, pos, facing);
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = this.getDefaultState().withProperty(POWERED, false);

        if (canPlaceWithOffset(worldIn, pos, facing.getOpposite()))
            return iblockstate.withProperty(FACING, EnumOrientation.forFacings(facing, placer.getHorizontalFacing()));
        else
        {
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                if (enumfacing != facing && canPlaceWithOffset(worldIn, pos, enumfacing.getOpposite()))
                    return iblockstate.withProperty(FACING, EnumOrientation.forFacings(enumfacing, placer.getHorizontalFacing()));

            if (World.doesBlockHaveSolidTopSurface(worldIn, pos.down()))
                return iblockstate.withProperty(FACING, EnumOrientation.forFacings(EnumFacing.UP, placer.getHorizontalFacing()));
            else
                return iblockstate;
        }
    }

    public static int getMetadataForFacing(EnumFacing facing)
    {
        switch (facing)
        {
            case DOWN:
                return 0;

            case UP:
                return 5;

            case NORTH:
                return 4;

            case SOUTH:
                return 3;

            case WEST:
                return 2;

            case EAST:
                return 1;

            default:
                return -1;
        }
    }

    /**
     * Called when a neighboring block changes.
     */
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (this.tryPlace(worldIn, pos, state) && !canPlaceWithOffset(worldIn, pos, state.getValue(FACING).getFacing().getOpposite()))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean tryPlace(World world, BlockPos pos, IBlockState state)
    {
        if (this.canPlaceBlockAt(world, pos))
            return true;
        else
        {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
            return false;
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        switch ((BlockLever.EnumOrientation)worldIn.getBlockState(pos).getValue(FACING))
        {
            case EAST:
                this.setBlockBounds(0.0F, 0.2F, 0.3125f, 0.375f, 0.8F, 0.6875f);
                break;

            case WEST:
                this.setBlockBounds(0.625f, 0.2F, 0.3125f, 1.0F, 0.8F, 0.6875f);
                break;

            case SOUTH:
                this.setBlockBounds(0.3125f, 0.2F, 0.0F, 0.6875f, 0.8F, 0.375f);
                break;

            case NORTH:
                this.setBlockBounds(0.3125f, 0.2F, 0.625f, 0.6875f, 0.8F, 1.0F);
                break;

            case UP_Z:
            case UP_X:
                this.setBlockBounds(0.25f, 0.0F, 0.25f, 0.75f, 0.6F, 0.75f);
                break;

            case DOWN_X:
            case DOWN_Z:
                this.setBlockBounds(0.25f, 0.4F, 0.25f, 0.75f, 1.0F, 0.75f);
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
            return true;
        else
        {
            state = state.cycleProperty(POWERED);
            worldIn.setBlockState(pos, state, 3);
            worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 
            		"random.click", 0.3F, state.getValue(POWERED) ? 0.6F : 0.5F);
            worldIn.notifyNeighborsOfStateChange(pos, this);
            EnumFacing enumfacing = state.getValue(FACING).getFacing();
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this);
            return true;
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getValue(POWERED))
        {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            EnumFacing enumfacing = state.getValue(FACING).getFacing();
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return state.getValue(POWERED) ? 15 : 0;
    }

    public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return !state.getValue(POWERED) ? 0 : (state.getValue(FACING).getFacing() == side ? 15 : 0);
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumOrientation.byMetadata(meta & 7)).withProperty(POWERED, (meta & 8) > 0);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(FACING).getMetadata();

        if (state.getValue(POWERED))
            i |= 8;

        return i;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING, POWERED});
    }

    public static enum EnumOrientation implements IStringSerializable
    {
        DOWN_X(0, "down_x", EnumFacing.DOWN),
        EAST(1, "east", EnumFacing.EAST),
        WEST(2, "west", EnumFacing.WEST),
        SOUTH(3, "south", EnumFacing.SOUTH),
        NORTH(4, "north", EnumFacing.NORTH),
        UP_Z(5, "up_z", EnumFacing.UP),
        UP_X(6, "up_x", EnumFacing.UP),
        DOWN_Z(7, "down_z", EnumFacing.DOWN);

        private static final EnumOrientation[] META_LOOKUP = new EnumOrientation[values().length];
        private final int meta;
        private final String name;
        private final EnumFacing facing;

        private EnumOrientation(int meta, String name, EnumFacing facing)
        {
            this.meta = meta;
            this.name = name;
            this.facing = facing;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public EnumFacing getFacing()
        {
            return this.facing;
        }

        public String toString()
        {
            return this.name;
        }

        public static EnumOrientation byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
                meta = 0;

            return META_LOOKUP[meta];
        }

        public static EnumOrientation forFacings(EnumFacing clickedSide, EnumFacing entityFacing)
        {
            switch (clickedSide)
            {
                case DOWN:
                    switch (entityFacing.getAxis())
                    {
                        case X:
                            return DOWN_X;

                        case Z:
                            return DOWN_Z;

                        default:
                            throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
                    }

                case UP:
                    switch (entityFacing.getAxis())
                    {
                        case X:
                            return UP_X;

                        case Z:
                            return UP_Z;

                        default:
                            throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
                    }

                case NORTH:
                    return NORTH;

                case SOUTH:
                    return SOUTH;

                case WEST:
                    return WEST;

                case EAST:
                    return EAST;

                default:
                    throw new IllegalArgumentException("Invalid facing: " + clickedSide);
            }
        }

        public String getName()
        {
            return this.name;
        }

        static {
            for (EnumOrientation blocklever$enumorientation : values())
                META_LOOKUP[blocklever$enumorientation.getMetadata()] = blocklever$enumorientation;
        }
    }
}

package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRailBase extends Block
{
    protected final boolean isPowered;

    public static boolean isRailBlock(World worldIn, BlockPos pos)
    {
        return isRailBlock(worldIn.getBlockState(pos));
    }

    public static boolean isRailBlock(IBlockState state)
    {
        Block block = state.getBlock();
        return block == Blocks.rail || block == Blocks.golden_rail || block == Blocks.detector_rail || block == Blocks.activator_rail;
    }

    protected BlockRailBase(boolean isPowered)
    {
        super(Material.circuits);
        this.isPowered = isPowered;
        this.setBlockBounds(0f, 0f, 0f, 1f, 0.125F, 1f);
        this.setCreativeTab(CreativeTabs.tabTransport);
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

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
     */
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() == this ?
        		iblockstate.getValue(this.getShapeProperty()) : null;

        if (blockrailbase$enumraildirection != null && blockrailbase$enumraildirection.isAscending())
            this.setBlockBounds(0f, 0f, 0f, 1f, 0.625F, 1f);
        else
            this.setBlockBounds(0f, 0f, 0f, 1f, 0.125F, 1f);
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            state = this.placeRail(worldIn, pos, state, true);

            if (this.isPowered)
                this.onNeighborBlockChange(worldIn, pos, state, this);
        }
    }

    /**
     * Called when a neighboring block changes.
     */
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!worldIn.isRemote)
        {
            EnumRailDirection railDirec = state.getValue(this.getShapeProperty());

            if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())
            	|| (railDirec == EnumRailDirection.ASCENDING_EAST && !World.doesBlockHaveSolidTopSurface(worldIn, pos.east()))
            	|| (railDirec == EnumRailDirection.ASCENDING_WEST && !World.doesBlockHaveSolidTopSurface(worldIn, pos.west()))
            	|| (railDirec == EnumRailDirection.ASCENDING_NORTH && !World.doesBlockHaveSolidTopSurface(worldIn, pos.north()))
            	|| (railDirec == EnumRailDirection.ASCENDING_SOUTH && !World.doesBlockHaveSolidTopSurface(worldIn, pos.south())))
            {
            	this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }else
                this.onNeighborChangedInternal(worldIn, pos, state, neighborBlock);
        }
    }

    protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
    }

    protected IBlockState placeRail(World worldIn, BlockPos pos, IBlockState state, boolean forcePlace)
    {
        return worldIn.isRemote ? state : (new Rail(worldIn, pos, state)).place(worldIn.isBlockPowered(pos), forcePlace).getBlockState();
    }

    public int getMobilityFlag()
    {
        return 0;
    }

    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);

        if (state.getValue(this.getShapeProperty()).isAscending())
            worldIn.notifyNeighborsOfStateChange(pos.up(), this);

        if (this.isPowered)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);
        }
    }

    public abstract IProperty<EnumRailDirection> getShapeProperty();

    public static enum EnumRailDirection implements IStringSerializable
    {
        NORTH_SOUTH(0, "north_south"),
        EAST_WEST(1, "east_west"),
        ASCENDING_EAST(2, "ascending_east"),
        ASCENDING_WEST(3, "ascending_west"),
        ASCENDING_NORTH(4, "ascending_north"),
        ASCENDING_SOUTH(5, "ascending_south"),
        SOUTH_EAST(6, "south_east"),
        SOUTH_WEST(7, "south_west"),
        NORTH_WEST(8, "north_west"),
        NORTH_EAST(9, "north_east");

        private static final EnumRailDirection[] META_LOOKUP = new EnumRailDirection[values().length];
        private final int meta;
        private final String name;

        private EnumRailDirection(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.name;
        }

        public boolean isAscending()
        {
            return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
        }

        public static EnumRailDirection byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return this.name;
        }

        static {
            for (EnumRailDirection blockrailbase$enumraildirection : values())
                META_LOOKUP[blockrailbase$enumraildirection.getMetadata()] = blockrailbase$enumraildirection;
        }
    }

    public class Rail
    {
        private final World world;
        private final BlockPos pos;
        private final BlockRailBase block;
        private IBlockState state;
        private final boolean isPowered;
        private final List<BlockPos> connectedRails = Lists.<BlockPos>newArrayList();

        public Rail(World worldIn, BlockPos pos, IBlockState state)
        {
            this.world = worldIn;
            this.pos = pos;
            this.state = state;
            this.block = (BlockRailBase)state.getBlock();
            EnumRailDirection direc = state.getValue(BlockRailBase.this.getShapeProperty());
            this.isPowered = this.block.isPowered;
            this.setAdjacentsFromDirection(direc);
        }

        private void setAdjacentsFromDirection(EnumRailDirection railDirection)
        {
            this.connectedRails.clear();

            switch (railDirection)
            {
                case NORTH_SOUTH:
                    this.connectedRails.add(this.pos.north());
                    this.connectedRails.add(this.pos.south());
                    break;

                case EAST_WEST:
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.east());
                    break;

                case ASCENDING_EAST:
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.east().up());
                    break;

                case ASCENDING_WEST:
                    this.connectedRails.add(this.pos.west().up());
                    this.connectedRails.add(this.pos.east());
                    break;

                case ASCENDING_NORTH:
                    this.connectedRails.add(this.pos.north().up());
                    this.connectedRails.add(this.pos.south());
                    break;

                case ASCENDING_SOUTH:
                    this.connectedRails.add(this.pos.north());
                    this.connectedRails.add(this.pos.south().up());
                    break;

                case SOUTH_EAST:
                    this.connectedRails.add(this.pos.east());
                    this.connectedRails.add(this.pos.south());
                    break;

                case SOUTH_WEST:
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.south());
                    break;

                case NORTH_WEST:
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.north());
                    break;

                case NORTH_EAST:
                    this.connectedRails.add(this.pos.east());
                    this.connectedRails.add(this.pos.north());
            }
        }

        private void refresh()
        {
            for (int i = 0; i < this.connectedRails.size(); ++i)
            {
                Rail rail = this.findRailAt((BlockPos)this.connectedRails.get(i));

                if (rail != null && rail.isRailIn(this))
                    this.connectedRails.set(i, rail.pos);
                else
                    this.connectedRails.remove(i--);
            }
        }

        private boolean hasRailAt(BlockPos pos)
        {
            return BlockRailBase.isRailBlock(this.world, pos) 
            	|| BlockRailBase.isRailBlock(this.world, pos.up()) 
            	|| BlockRailBase.isRailBlock(this.world, pos.down());
        }

        private Rail findRailAt(BlockPos pos)
        {
            IBlockState iblockstate = this.world.getBlockState(pos);

            if (BlockRailBase.isRailBlock(iblockstate))
                return new Rail(this.world, pos, iblockstate);
            else
            {
                BlockPos posUp = pos.up();
                iblockstate = this.world.getBlockState(posUp);

                if (BlockRailBase.isRailBlock(iblockstate))
                    return new Rail(this.world, posUp, iblockstate);
                else
                {
                    posUp = pos.down();
                    iblockstate = this.world.getBlockState(posUp);
                    return BlockRailBase.isRailBlock(iblockstate) ? new Rail(this.world, posUp, iblockstate) : null;
                }
            }
        }

        private boolean isRailIn(Rail rail)
        {
            return this.railsContain(rail.pos);
        }

        private boolean railsContain(BlockPos pos)
        {
            for (int i = 0; i < this.connectedRails.size(); ++i)
            {
                BlockPos blockpos = (BlockPos)this.connectedRails.get(i);

                if (blockpos.getX() == pos.getX() && blockpos.getZ() == pos.getZ())
                    return true;
            }

            return false;
        }

        protected int countAdjacentRails()
        {
            int i = 0;

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                if (this.hasRailAt(this.pos.offset(enumfacing)))
                    ++i;

            return i;
        }

        /**
         * VoidAlchemist's notes : I don't get the purpose of this function at all.
         * @param rail
         * @return
         */
        private boolean func_150649_b(Rail rail)
        {
            return this.isRailIn(rail) || this.connectedRails.size() != 2;
        }

        private void updateForRail(Rail rail)
        {
            this.connectedRails.add(rail.pos);
            BlockPos blockpos = this.pos.north();
            BlockPos blockpos1 = this.pos.south();
            BlockPos blockpos2 = this.pos.west();
            BlockPos blockpos3 = this.pos.east();
            boolean flag = this.railsContain(blockpos);
            boolean flag1 = this.railsContain(blockpos1);
            boolean flag2 = this.railsContain(blockpos2);
            boolean flag3 = this.railsContain(blockpos3);
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = null;

            if (flag || flag1)
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;

            if (flag2 || flag3)
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;

            if (!this.isPowered)
            {
                if (flag1 && flag3 && !flag && !flag2)
                
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                

                if (flag1 && flag2 && !flag && !flag3)
                
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                

                if (flag && flag2 && !flag1 && !flag3)
                
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
                

                if (flag && flag3 && !flag1 && !flag2)
                
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
                
            }

            if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH)
            {
                if (BlockRailBase.isRailBlock(this.world, blockpos.up()))
                
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
                

                if (BlockRailBase.isRailBlock(this.world, blockpos1.up()))
                
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
                
            }

            if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST)
            {
                if (BlockRailBase.isRailBlock(this.world, blockpos3.up()))
                
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
                

                if (BlockRailBase.isRailBlock(this.world, blockpos2.up()))
                
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
                
            }

            if (blockrailbase$enumraildirection == null)
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;

            this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);
            this.world.setBlockState(this.pos, this.state, 3);
        }

        private boolean updatePos(BlockPos pos)
        {
            Rail rail = this.findRailAt(pos);

            if (rail == null)
                return false;
            else
            {
                rail.refresh();
                return rail.func_150649_b(this);
            }
        }

        /**
         * 
         * @param unknown VoidAlchemist's notes : doesn't do anything. normally appears once, but its value does not semantically change a thing.
         * @param forcePlace VoidAlchemist's notes : most plausible name for this boolean.
         * @return
         */
        public Rail place(boolean unknown, boolean forcePlace)
        {
            BlockPos blockpos = this.pos.north();
            BlockPos blockpos1 = this.pos.south();
            BlockPos blockpos2 = this.pos.west();
            BlockPos blockpos3 = this.pos.east();
            boolean flag = this.updatePos(blockpos);
            boolean flag1 = this.updatePos(blockpos1);
            boolean flag2 = this.updatePos(blockpos2);
            boolean flag3 = this.updatePos(blockpos3);
            EnumRailDirection railDirection = null;

            if ((flag || flag1) && !flag2 && !flag3)
                railDirection = EnumRailDirection.NORTH_SOUTH;

            if ((flag2 || flag3) && !flag && !flag1)
                railDirection = EnumRailDirection.EAST_WEST;

            if (!this.isPowered)
            {
                if (flag1 && flag3 && !flag && !flag2)
                
                    railDirection = EnumRailDirection.SOUTH_EAST;
                

                if (flag1 && flag2 && !flag && !flag3)
                
                    railDirection = EnumRailDirection.SOUTH_WEST;
                

                if (flag && flag2 && !flag1 && !flag3)
                
                    railDirection = EnumRailDirection.NORTH_WEST;
                

                if (flag && flag3 && !flag1 && !flag2)
                
                    railDirection = EnumRailDirection.NORTH_EAST;
                
            }

            if (railDirection == null)
            {
                if (flag || flag1)
                
                    railDirection = EnumRailDirection.NORTH_SOUTH;
                

                if (flag2 || flag3)
                
                    railDirection = EnumRailDirection.EAST_WEST;
                

                if (!this.isPowered)
                {
                	if (flag1 && flag3)
                        
                        railDirection = EnumRailDirection.SOUTH_EAST;
                    

                    if (flag2 && flag1)
                    
                        railDirection = EnumRailDirection.SOUTH_WEST;
                    

                    if (flag3 && flag)
                    
                        railDirection = EnumRailDirection.NORTH_EAST;
                    

                    if (flag && flag2)
                    
                        railDirection = EnumRailDirection.NORTH_WEST;
                }
            }

            if (railDirection == EnumRailDirection.NORTH_SOUTH)
            {
                if (isRailBlock(this.world, blockpos.up()))
                
                    railDirection = EnumRailDirection.ASCENDING_NORTH;
                

                if (isRailBlock(this.world, blockpos1.up()))
                
                    railDirection = EnumRailDirection.ASCENDING_SOUTH;
                
            }

            if (railDirection == EnumRailDirection.EAST_WEST)
            {
                if (BlockRailBase.isRailBlock(this.world, blockpos3.up()))
                
                    railDirection = EnumRailDirection.ASCENDING_EAST;
                

                if (BlockRailBase.isRailBlock(this.world, blockpos2.up()))
                
                    railDirection = EnumRailDirection.ASCENDING_WEST;
                
            }

            if (railDirection == null)
                railDirection = EnumRailDirection.NORTH_SOUTH;

            this.setAdjacentsFromDirection(railDirection);
            this.state = this.state.withProperty(this.block.getShapeProperty(), railDirection);

            if (forcePlace || this.world.getBlockState(this.pos) != this.state)
            {
                this.world.setBlockState(this.pos, this.state, 3);

                for (int i = 0; i < this.connectedRails.size(); ++i)
                {
                    BlockRailBase.Rail rail = this.findRailAt((BlockPos)this.connectedRails.get(i));

                    if (rail != null)
                    {
                        rail.refresh();

                        if (rail.func_150649_b(this))
                            rail.updateForRail(this);
                    }
                }
            }

            return this;
        }

        public IBlockState getBlockState()
        {
            return this.state;
        }
    }
}

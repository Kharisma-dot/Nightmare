package net.minecraft.block;

import com.google.common.cache.LoadingCache;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortal extends BlockBreakable
{
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.<EnumFacing.Axis>create("axis", EnumFacing.Axis.class,
    		new EnumFacing.Axis[] {EnumFacing.Axis.X, EnumFacing.Axis.Z});

    public BlockPortal()
    {
        super(Material.portal, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.X));
        this.setTickRandomly(true);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);

        if (worldIn.provider.isSurfaceWorld() && worldIn.getGameRules().getBoolean("doMobSpawning") 
        		&& rand.nextInt(2000) < worldIn.getDifficulty().getDifficultyId())
        {
            int i = pos.getY();
            BlockPos blockpos;

            for (blockpos = pos; !World.doesBlockHaveSolidTopSurface(worldIn, blockpos) && blockpos.getY() > 0; blockpos = blockpos.down())
                ;

            if (i > 0 && !worldIn.getBlockState(blockpos.up()).getBlock().isNormalCube())
            {
                Entity entity = ItemMonsterPlacer.spawnCreature(worldIn, 57, 
                		(double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 1.1D, (double)blockpos.getZ() + 0.5D);

                if (entity != null)
                    entity.timeUntilPortal = entity.getPortalCooldown();
            }
        }
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        EnumFacing.Axis axis = worldIn.getBlockState(pos).getValue(AXIS);
        float f = axis == EnumFacing.Axis.X ? 0.5f : 0.125f;
        float f1 = axis == EnumFacing.Axis.Z ? 0.5f : 0.125f;

        this.setBlockBounds(0.5F - f, 0f, 0.5F - f1, 0.5F + f, 1f, 0.5F + f1);
    }

    public static int getMetaForAxis(EnumFacing.Axis axis)
    {
        return axis == EnumFacing.Axis.X ? 1 : (axis == EnumFacing.Axis.Z ? 2 : 0);
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean createPortal(World worldIn, BlockPos pos)
    {
        BlockPortal.Size blockportal$size = new BlockPortal.Size(worldIn, pos, EnumFacing.Axis.X);

        if (blockportal$size.isValid() && blockportal$size.portalBlocksCount == 0)
        {
            blockportal$size.placePortalBlocks();
            return true;
        }
        else
        {
            BlockPortal.Size blockportal$size1 = new BlockPortal.Size(worldIn, pos, EnumFacing.Axis.Z);

            if (blockportal$size1.isValid() && blockportal$size1.portalBlocksCount == 0)
            {
                blockportal$size1.placePortalBlocks();
                return true;
            }
            else
                return false;
        }
    }

    /**
     * Called when a neighboring block changes.
     */
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        EnumFacing.Axis enumfacing$axis = state.getValue(AXIS);

        if (enumfacing$axis == EnumFacing.Axis.X)
        {
            BlockPortal.Size blockportal$size = new BlockPortal.Size(worldIn, pos, EnumFacing.Axis.X);

            if (!blockportal$size.isValid() || blockportal$size.portalBlocksCount < blockportal$size.width * blockportal$size.height)
                worldIn.setBlockState(pos, Blocks.air.getDefaultState());
        }
        else if (enumfacing$axis == EnumFacing.Axis.Z)
        {
            BlockPortal.Size blockportal$size1 = new BlockPortal.Size(worldIn, pos, EnumFacing.Axis.Z);

            if (!blockportal$size1.isValid() || blockportal$size1.portalBlocksCount < blockportal$size1.width * blockportal$size1.height)
                worldIn.setBlockState(pos, Blocks.air.getDefaultState());
        }
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        EnumFacing.Axis axis;
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (worldIn.getBlockState(pos).getBlock() == this)
        {
            axis = iblockstate.getValue(AXIS);

            if (axis == null
            	|| (axis == EnumFacing.Axis.Z && side != EnumFacing.EAST && side != EnumFacing.WEST)
            	|| (axis == EnumFacing.Axis.X && side != EnumFacing.SOUTH && side != EnumFacing.NORTH))
                return false;
        }else
        	axis = null;

        boolean flag = worldIn.getBlockState(pos.west()).getBlock() == this && worldIn.getBlockState(pos.west(2)).getBlock() != this;
        boolean flag1 = worldIn.getBlockState(pos.east()).getBlock() == this && worldIn.getBlockState(pos.east(2)).getBlock() != this;
        boolean flag2 = worldIn.getBlockState(pos.north()).getBlock() == this && worldIn.getBlockState(pos.north(2)).getBlock() != this;
        boolean flag3 = worldIn.getBlockState(pos.south()).getBlock() == this && worldIn.getBlockState(pos.south(2)).getBlock() != this;
        boolean flag4 = flag || flag1 || axis == EnumFacing.Axis.X;
        boolean flag5 = flag2 || flag3 || axis == EnumFacing.Axis.Z;
        return flag4 && side == EnumFacing.WEST ? true : (flag4 && side == EnumFacing.EAST ? true : (flag5 && side == EnumFacing.NORTH ? true : flag5 && side == EnumFacing.SOUTH));
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }

    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    /**
     * Called When an Entity Collided with the Block
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (entityIn.ridingEntity == null && entityIn.riddenByEntity == null)
            entityIn.setPortal(pos);
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (rand.nextInt(100) == 0)
            worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 
            		"portal.portal", 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);

        for (int i = 0; i < 4; ++i)
        {
            double d0 = (double)((float)pos.getX() + rand.nextFloat());
            double d1 = (double)((float)pos.getY() + rand.nextFloat());
            double d2 = (double)((float)pos.getZ() + rand.nextFloat());
            double d3 = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            double d4 = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            double d5 = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            int j = rand.nextInt(2) * 2 - 1;

            if (worldIn.getBlockState(pos.west()).getBlock() != this && worldIn.getBlockState(pos.east()).getBlock() != this)
            {
                d0 = (double)pos.getX() + 0.5D + 0.25D * (double)j;
                d3 = (double)(rand.nextFloat() * 2.0F * (float)j);
            }
            else
            {
                d2 = (double)pos.getZ() + 0.5D + 0.25D * (double)j;
                d5 = (double)(rand.nextFloat() * 2.0F * (float)j);
            }

            worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }

    public Item getItem(World worldIn, BlockPos pos)
    {
        return null;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AXIS, (meta & 3) == 2 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return getMetaForAxis(state.getValue(AXIS));
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {AXIS});
    }

    public BlockPattern.PatternHelper func_181089_f(World world, BlockPos pos)
    {
        EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Z;
        BlockPortal.Size blockportal$size = new BlockPortal.Size(world, pos, EnumFacing.Axis.X);
        LoadingCache<BlockPos, BlockWorldState> loadingcache = BlockPattern.load(world, true);

        if (!blockportal$size.isValid())
        {
            enumfacing$axis = EnumFacing.Axis.X;
            blockportal$size = new BlockPortal.Size(world, pos, EnumFacing.Axis.Z);
        }

        if (!blockportal$size.isValid())
        {
            return new BlockPattern.PatternHelper(pos, EnumFacing.NORTH, EnumFacing.UP, loadingcache, 1, 1, 1);
        }
        else
        {
            int[] aint = new int[EnumFacing.AxisDirection.values().length];
            EnumFacing enumfacing = blockportal$size.facingRight.rotateYCCW();
            BlockPos blockpos = blockportal$size.posStartLeft.up(blockportal$size.getHeight() - 1);

            for (EnumFacing.AxisDirection enumfacing$axisdirection : EnumFacing.AxisDirection.values())
            {
                BlockPattern.PatternHelper blockpattern$patternhelper = new BlockPattern.PatternHelper(enumfacing.getAxisDirection() == enumfacing$axisdirection ? blockpos : blockpos.offset(blockportal$size.facingRight, blockportal$size.getWidth() - 1), EnumFacing.getFacingFromAxis(enumfacing$axisdirection, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);

                for (int i = 0; i < blockportal$size.getWidth(); ++i)
                    for (int j = 0; j < blockportal$size.getHeight(); ++j)
                    {
                        BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, j, 1);

                        if (blockworldstate.getBlockState() != null && blockworldstate.getBlockState().getBlock().getMaterial() != Material.air)
                            ++aint[enumfacing$axisdirection.ordinal()];
                    }
            }

            EnumFacing.AxisDirection enumfacing$axisdirection1 = EnumFacing.AxisDirection.POSITIVE;

            for (EnumFacing.AxisDirection enumfacing$axisdirection2 : EnumFacing.AxisDirection.values())
                if (aint[enumfacing$axisdirection2.ordinal()] < aint[enumfacing$axisdirection1.ordinal()])
                    enumfacing$axisdirection1 = enumfacing$axisdirection2;

            return new BlockPattern.PatternHelper(enumfacing.getAxisDirection() == enumfacing$axisdirection1 ?
            		blockpos : blockpos.offset(blockportal$size.facingRight, blockportal$size.getWidth() - 1),
            		EnumFacing.getFacingFromAxis(enumfacing$axisdirection1, enumfacing$axis), EnumFacing.UP, loadingcache,
            		blockportal$size.getWidth(), blockportal$size.getHeight(), 1);
        }
    }

    public static class Size
    {
        private final World world;
        private final EnumFacing.Axis axis;
        private final EnumFacing facingRight;
        private final EnumFacing facingLeft;
        private int portalBlocksCount = 0;
        private BlockPos posStartLeft;
        private int height;
        private int width;

        public Size(World worldIn, BlockPos pos, EnumFacing.Axis axis)
        {
            this.world = worldIn;
            this.axis = axis;

            if (axis == EnumFacing.Axis.X)
            {
                this.facingLeft = EnumFacing.EAST;
                this.facingRight = EnumFacing.WEST;
            }
            else
            {
                this.facingLeft = EnumFacing.NORTH;
                this.facingRight = EnumFacing.SOUTH;
            }

            for (BlockPos blockpos = pos; pos.getY() > blockpos.getY() - 21 && pos.getY() > 0 
            		&& this.isEmptyBlock(worldIn.getBlockState(pos.down()).getBlock()); pos = pos.down())
                ;

            int i = this.getDistanceUntilEdge(pos, this.facingLeft) - 1;

            if (i >= 0)
            {
                this.posStartLeft = pos.offset(this.facingLeft, i);
                this.width = this.getDistanceUntilEdge(this.posStartLeft, this.facingRight);

                if (this.width < 2 || this.width > 21)
                {
                    this.posStartLeft = null;
                    this.width = 0;
                }
            }

            if (this.posStartLeft != null)
                this.height = this.calculatePortalHeight();
        }

        protected int getDistanceUntilEdge(BlockPos pos, EnumFacing facing)
        {
            int i;

            for (i = 0; i < 22; ++i)
            {
                BlockPos blockpos = pos.offset(facing, i);

                if (!this.isEmptyBlock(this.world.getBlockState(blockpos).getBlock()) 
                		|| this.world.getBlockState(blockpos.down()).getBlock() != Blocks.obsidian)
                    break;
            }

            Block block = this.world.getBlockState(pos.offset(facing, i)).getBlock();
            return block == Blocks.obsidian ? i : 0;
        }

        public int getHeight()
        {
            return this.height;
        }

        public int getWidth()
        {
            return this.width;
        }

        protected int calculatePortalHeight()
        {
            label24:

            for (this.height = 0; this.height < 21; ++this.height)
            {
                for (int i = 0; i < this.width; ++i)
                {
                    BlockPos blockpos = this.posStartLeft.offset(this.facingRight, i).up(this.height);
                    Block block = this.world.getBlockState(blockpos).getBlock();

                    if (!this.isEmptyBlock(block))
                        break label24;

                    if (block == Blocks.portal)
                        ++this.portalBlocksCount;

                    if (i == 0)
                    {
                        block = this.world.getBlockState(blockpos.offset(this.facingLeft)).getBlock();

                        if (block != Blocks.obsidian)
                            break label24;
                    }
                    else if (i == this.width - 1)
                    {
                        block = this.world.getBlockState(blockpos.offset(this.facingRight)).getBlock();

                        if (block != Blocks.obsidian)
                            break label24;
                    }
                }
            }

            for (int j = 0; j < this.width; ++j)
                if (this.world.getBlockState(this.posStartLeft.offset(this.facingRight, j).up(this.height)).getBlock() != Blocks.obsidian)
                {
                    this.height = 0;
                    break;
                }

            if (this.height <= 21 && this.height >= 3)
                return this.height;
            else
            {
                this.posStartLeft = null;
                this.width = 0;
                this.height = 0;
                return 0;
            }
        }

        protected boolean isEmptyBlock(Block block)
        {
            return block.blockMaterial == Material.air || block == Blocks.fire || block == Blocks.portal;
        }

        public boolean isValid()
        {
            return this.posStartLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
        }

        public void placePortalBlocks()
        {
            for (int i = 0; i < this.width; ++i)
            {
                BlockPos blockpos = this.posStartLeft.offset(this.facingRight, i);

                for (int j = 0; j < this.height; ++j)
                    this.world.setBlockState(blockpos.up(j), Blocks.portal.getDefaultState().withProperty(BlockPortal.AXIS, this.axis), 2);
            }
        }
    }
}

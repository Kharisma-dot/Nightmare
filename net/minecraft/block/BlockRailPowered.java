package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRailPowered extends BlockRailBase
{
    public static final PropertyEnum<EnumRailDirection> SHAPE = PropertyEnum.<EnumRailDirection>create("shape", EnumRailDirection.class, (direc) ->
    direc != EnumRailDirection.NORTH_EAST && direc != EnumRailDirection.NORTH_WEST 
    && direc != EnumRailDirection.SOUTH_EAST && direc != EnumRailDirection.SOUTH_WEST);
    
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    protected BlockRailPowered()
    {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE, EnumRailDirection.NORTH_SOUTH)
        		.withProperty(POWERED, false));
    }

    protected boolean checkNearPoweredRails(World worldIn, BlockPos pos, IBlockState state, boolean forward, int distance)
    {
        if (distance >= 8)
            return false;
        else
        {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            boolean flag = true;
            EnumRailDirection railDirection = state.getValue(SHAPE);

            switch (railDirection)
            {
                case NORTH_SOUTH:
                    if (forward)
                        ++k;
                    else
                        --k;

                    break;

                case EAST_WEST:
                    if (forward)
                    
                        --i;
                    
                    else
                    
                        ++i;
                    

                    break;

                case ASCENDING_EAST:
                    if (forward)
                    
                        --i;
                    
                    else
                    {
                        ++i;
                        ++j;
                        flag = false;
                    }

                    railDirection = EnumRailDirection.EAST_WEST;
                    break;

                case ASCENDING_WEST:
                    if (forward)
                    {
                        --i;
                        ++j;
                        flag = false;
                    }
                    else
                    
                        ++i;
                    

                    railDirection = EnumRailDirection.EAST_WEST;
                    break;

                case ASCENDING_NORTH:
                    if (forward)
                    
                        ++k;
                    
                    else
                    {
                        --k;
                        ++j;
                        flag = false;
                    }

                    railDirection = EnumRailDirection.NORTH_SOUTH;
                    break;

                case ASCENDING_SOUTH:
                    if (forward)
                    {
                        ++k;
                        ++j;
                        flag = false;
                    }
                    else
                    
                        --k;
                    railDirection = EnumRailDirection.NORTH_SOUTH;
                    break;
                default:
            }

            return this.isPowered(worldIn, new BlockPos(i, j, k), forward, distance, railDirection) ?
            		true : flag && this.isPowered(worldIn, new BlockPos(i, j - 1, k), forward, distance, railDirection);
        }
    }

    protected boolean isPowered(World worldIn, BlockPos pos, boolean p_176567_3_, int distance, EnumRailDirection railDirection)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() != this)
            return false;
        else
        {
            EnumRailDirection direc = iblockstate.getValue(SHAPE);
            return railDirection != EnumRailDirection.EAST_WEST || direc != EnumRailDirection.NORTH_SOUTH 
            		&& direc != EnumRailDirection.ASCENDING_NORTH && direc != EnumRailDirection.ASCENDING_SOUTH ?
            		(railDirection != EnumRailDirection.NORTH_SOUTH || direc != EnumRailDirection.EAST_WEST 
            		&& direc != EnumRailDirection.ASCENDING_EAST && direc != EnumRailDirection.ASCENDING_WEST ?
            		(iblockstate.getValue(POWERED) ? (worldIn.isBlockPowered(pos) ?
            				true : this.checkNearPoweredRails(worldIn, pos, iblockstate, p_176567_3_, distance + 1)) : false) : false) : false;
        }
    }

    protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        boolean flag = state.getValue(POWERED);
        boolean flag1 = worldIn.isBlockPowered(pos) || this.checkNearPoweredRails(worldIn, pos, state, true, 0) ||
        		this.checkNearPoweredRails(worldIn, pos, state, false, 0);

        if (flag1 != flag)
        {
            worldIn.setBlockState(pos, state.withProperty(POWERED, flag1), 3);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);

            if (state.getValue(SHAPE).isAscending())
                worldIn.notifyNeighborsOfStateChange(pos.up(), this);
        }
    }

    public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty()
    {
        return SHAPE;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(SHAPE, EnumRailDirection.byMetadata(meta & 7))
        		.withProperty(POWERED, (meta & 8) > 0);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(SHAPE).getMetadata();

        if (state.getValue(POWERED))
            i |= 8;

        return i;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {SHAPE, POWERED});
    }
}

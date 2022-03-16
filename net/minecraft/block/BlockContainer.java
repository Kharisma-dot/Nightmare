package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class BlockContainer extends Block implements ITileEntityProvider
{
    protected BlockContainer(Material materialIn)
    {
        this(materialIn, materialIn.getMaterialMapColor());
    }

    protected BlockContainer(Material material, MapColor mapColor)
    {
        super(material, mapColor);
        this.isBlockContainer = true;
    }

    protected boolean isInvalidNeighbor(World world, BlockPos pos, EnumFacing facing)
    {
        return world.getBlockState(pos.offset(facing)).getBlock().getMaterial() == Material.cactus;
    }

    protected boolean hasInvalidNeighbor(World world, BlockPos pos)
    {
        return this.isInvalidNeighbor(world, pos, EnumFacing.NORTH) || this.isInvalidNeighbor(world, pos, EnumFacing.SOUTH) || this.isInvalidNeighbor(world, pos, EnumFacing.WEST) || this.isInvalidNeighbor(world, pos, EnumFacing.EAST);
    }

    /**
     * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
     */
    public int getRenderType()
    {
        return -1;
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    /**
     * Called on both Client and Server when World#addBlockEvent is called
     */
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
    {
        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
    }
}

package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate
{
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    private final int maxRedstoneStrength;

    protected BlockPressurePlateWeighted(Material material, int maxRedstoneStrength)
    {
        this(material, maxRedstoneStrength, material.getMaterialMapColor());
    }

    protected BlockPressurePlateWeighted(Material material, int maxRedstoneStrength, MapColor mapColor)
    {
        super(material, mapColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, 0));
        this.maxRedstoneStrength = maxRedstoneStrength;
    }

    protected int computeRedstoneStrength(World worldIn, BlockPos pos)
    {
        int i = Math.min(worldIn.getEntitiesWithinAABB(Entity.class, this.getSensitiveAABB(pos)).size(), this.maxRedstoneStrength);

        if (i > 0)
        {
            float f = (float)Math.min(this.maxRedstoneStrength, i) / (float)this.maxRedstoneStrength;
            return MathHelper.ceiling_float_int(f * 15.0F);
        }
        else
            return 0;
    }

    protected int getRedstoneStrength(IBlockState state)
    {
        return state.getValue(POWER);
    }

    protected IBlockState setRedstoneStrength(IBlockState state, int strength)
    {
        return state.withProperty(POWER, strength);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 10;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWER, meta);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(POWER);
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {POWER});
    }
}

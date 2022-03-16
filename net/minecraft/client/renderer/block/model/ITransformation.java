package net.minecraft.client.renderer.block.model;

import javax.vecmath.Matrix4f;
import net.minecraft.util.EnumFacing;

public interface ITransformation
{
    Matrix4f getMatrix();

    EnumFacing rotate(EnumFacing facing);

    int rotate(EnumFacing facing, int var);
}

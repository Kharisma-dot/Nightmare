package net.optifine.reflect;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class ReflectorForge
{
    public static InputStream getOptiFineResourceStream(String path)
    {
        if (!Reflector.OptiFineClassTransformer_instance.exists())
            return null;
        else
        {
            Object object = Reflector.getFieldValue(Reflector.OptiFineClassTransformer_instance);

            if (object == null)
                return null;
            else
            {
                if (path.startsWith("/"))
                    path = path.substring(1);

                byte[] abyte = (byte[])((byte[])Reflector.call(object, Reflector.OptiFineClassTransformer_getOptiFineResource, new Object[] {path}));

                if (abyte == null)
                    return null;
                else
                {
                    InputStream inputstream = new ByteArrayInputStream(abyte);
                    return inputstream;
                }
            }
        }
    }
}

package nightmare.utils.render;

import java.io.IOException;

import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import com.google.gson.JsonSyntaxException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.util.JsonException;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import nightmare.Nightmare;
import nightmare.utils.ScreenUtils;

public class BlurUtils {
	
	private static ShaderGroup blurShader;
	private static Minecraft mc = Minecraft.getMinecraft();
	private static Framebuffer buffer;
	
    private static float lastScale = 0;
    private static float lastScaleWidth = 0;
    private static float lastScaleHeight = 0;
    
    private static void reinitShader() {
		try {
			buffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
			buffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
			blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), new ResourceLocation("shaders/post/blurArea.json"));
			blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void drawBlurRect(float x, float y, float width, float height) {
    	
        int factor = ScreenUtils.getScaleFactor();
        int factor2 = ScreenUtils.getWitdh();
        int factor3 = ScreenUtils.getHeight();
        
        float scale = 1;
        
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3) {
            reinitShader();
        }
        
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("BlurXY").set(x * (ScreenUtils.getScaleFactor() / 2.0F), (factor3 - height) * (ScreenUtils.getScaleFactor() / 2.0F));
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("BlurXY").set(x * (ScreenUtils.getScaleFactor() / 2.0F), (factor3 - height) * (ScreenUtils.getScaleFactor() / 2.0F));
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("BlurCoord").set((width - x) * (ScreenUtils.getScaleFactor() / 2.0F), (height - y) * (ScreenUtils.getScaleFactor() / 2.0F));
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("BlurCoord").set((width - x) * (ScreenUtils.getScaleFactor() / 2.0F), (height - y) * (ScreenUtils.getScaleFactor() / 2.0F));
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set((int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("Blur"), "Radius").getValDouble());
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set((int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("Blur"), "Radius").getValDouble());
        blurShader.loadShaderGroup(mc.timer.renderPartialTicks);
        mc.getFramebuffer().bindFramebuffer(true);
    }
}

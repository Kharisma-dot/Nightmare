package nightmare.module.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventRender3D;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.utils.ColorUtils;
import nightmare.utils.render.Render3DUtils;
import nightmare.utils.render.RenderUtils;

public class Trajectories extends Module{
	
	private boolean isBow;
    
	public Trajectories() {
		super("Trajectories", 0, Category.RENDER);
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		
		if(mc.theWorld == null) {
			return;
		}
		
		final ItemStack stack = mc.thePlayer.getHeldItem();
		if (stack == null || !isItemValid(stack)) return;
		
		isBow = stack.getItem() instanceof ItemBow;
		
		final double playerYaw = mc.thePlayer.rotationYaw;
		final double playerPitch = mc.thePlayer.rotationPitch;

		double projectilePosX = mc.getRenderManager().getRenderPosX() - Math.cos(Math.toRadians(playerYaw)) * .16F;
		double projectilePosY = mc.getRenderManager().getRenderPosY() + mc.thePlayer.getEyeHeight();
		double projectilePosZ = mc.getRenderManager().getRenderPosZ() - Math.sin(Math.toRadians(playerYaw)) * .16F;

		double projectileMotionX = (-Math.sin(Math.toRadians(playerYaw)) * Math.cos(Math.toRadians(playerPitch))) * (isBow ? 1 : .4);
		double projectileMotionY = -Math.sin(Math.toRadians(playerPitch - 0)) * (isBow ? 1 : .4);
		double projectileMotionZ = (Math.cos(Math.toRadians(playerYaw)) * Math.cos(Math.toRadians(playerPitch))) * (isBow ? 1 : .4);
		
		double shootPower = mc.thePlayer.getItemInUseDuration();
		
		if (isBow) {
			shootPower /= 20;
			shootPower = ((shootPower * shootPower) + (shootPower * 2)) / 3;
			
			if (shootPower < .1) return;
			if (shootPower > 1) shootPower = 1;
		}
		
		final double distance = Math.sqrt(projectileMotionX * projectileMotionX + projectileMotionY * projectileMotionY + projectileMotionZ * projectileMotionZ);
		
		projectileMotionX /= distance;
		projectileMotionY /= distance;
		projectileMotionZ /= distance;
		
		projectileMotionX *= (isBow ? shootPower : .5) * 3;
		projectileMotionY *= (isBow ? shootPower : .5) * 3;
		projectileMotionZ *= (isBow ? shootPower : .5) * 3;
		
		boolean projectileHasLanded = false;
		MovingObjectPosition landingPosition = null;
		
		GlStateManager.resetColor();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GlStateManager.disableTexture2D();
		RenderUtils.setColor(ColorUtils.getClientColor());
		GL11.glLineWidth(1.5F);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		{
			while (!projectileHasLanded && projectilePosY > 0) {
				final Vec3 currentPosition = new Vec3(projectilePosX, projectilePosY, projectilePosZ);
				final Vec3 nextPosition = new Vec3(projectilePosX + projectileMotionX, projectilePosY + projectileMotionY, projectilePosZ + projectileMotionZ);
				
				final MovingObjectPosition possibleLandingPositon = mc.theWorld.rayTraceBlocks(currentPosition, nextPosition, false, true, false);
				
				if (possibleLandingPositon != null) {
					if (possibleLandingPositon.typeOfHit != MovingObjectType.MISS) {
						landingPosition = possibleLandingPositon;
						projectileHasLanded = true;
					}
				}

				projectilePosX += projectileMotionX;
				projectilePosY += projectileMotionY;
				projectilePosZ += projectileMotionZ;

				projectileMotionX *= .99;
				projectileMotionY *= .99;
				projectileMotionZ *= .99;
				
				projectileMotionY -= (isBow ? .05 : .03); // Gravitation
				
				GL11.glVertex3d(projectilePosX - mc.getRenderManager().getRenderPosX(), projectilePosY - mc.getRenderManager().getRenderPosY(), projectilePosZ - mc.getRenderManager().getRenderPosZ());
			}
		}
		GL11.glEnd();
		GlStateManager.enableTexture2D();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);
		GlStateManager.resetColor();
		
		if (landingPosition != null) {
			if (landingPosition.typeOfHit == MovingObjectType.BLOCK) {
				Render3DUtils.drawBox(landingPosition.getBlockPos(), new Color((int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("HUD"), "Red").getValDouble(), (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("HUD"), "Green").getValDouble(), (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("HUD"), "Blue").getValDouble(), 180), false);
			}
		}
	}
	
	private boolean isItemValid(ItemStack stack) {
		return (stack.getItem() instanceof ItemBow) || (stack.getItem() instanceof ItemEnderPearl) || (stack.getItem() instanceof ItemEgg) || (stack.getItem() instanceof ItemSnowball);
	}
}

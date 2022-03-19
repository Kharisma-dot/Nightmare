package nightmare.module.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventRenderGUI;
import nightmare.fonts.impl.Fonts;
import nightmare.gui.GuiHudEditor;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.ColorUtils;
import nightmare.utils.render.BlurUtils;

public class TargetHUD extends Module{

	private static EntityLivingBase target = null;
    private NetworkPlayerInfo playerInfo;
    
	public TargetHUD() {
		super("TargetHUD", 0, Category.RENDER);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("Distance", this, 6.5F, 3, 15, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("X", this, 20, 0, 5000, true));
		Nightmare.instance.settingsManager.rSetting(new Setting("Y", this, 20, 0, 5000, true));
	}

	@EventTarget
	public void onRender(EventRenderGUI event) {
		
		int x = (int) Nightmare.instance.settingsManager.getSettingByName(this, "X").getValDouble();
		int y = (int) Nightmare.instance.settingsManager.getSettingByName(this, "Y").getValDouble();
		int hp = (int) (target == null ? 0 : (int) target.getHealth() + (int) target.getAbsorptionAmount() < 20 ? target.getHealth() + (int) target.getAbsorptionAmount() : 20);
		int armor = target == null ? 0 : target.getTotalArmorValue();
		int hpColor = new Color(110, 230, 125).getRGB();
		
		target = getClosest((float) Nightmare.instance.settingsManager.getSettingByName(this, "Distance").getValDouble());
		
		if(target == null && !(mc.currentScreen instanceof GuiHudEditor)) {
			return;
		}		
		
		if(mc.currentScreen instanceof GuiHudEditor) {
			Gui.drawRect(x - 2, y - 17, x + 130, y - 2, ColorUtils.getClientColor());
			Fonts.REGULAR.REGULAR_23.REGULAR_23.drawString("TargetHUD", x, y - 13, -1, false);
			target = mc.thePlayer;
		}
		
		if(target.getHealth() <= 20 && target.getHealth() > 12) {
			hpColor = new Color(110, 230, 125).getRGB();
		}else if(target.getHealth() <= 12 && target.getHealth() > 4) {
			hpColor = new Color(210, 230, 125).getRGB();
		}else if(target.getHealth() <= 4 && target.getHealth() > 0) {
			hpColor = new Color(220, 150, 110).getRGB();
		}
		if(!(mc.currentScreen instanceof GuiHudEditor) && target.getName().equals(mc.thePlayer.getName())) {
			return;
		}
		
		if(target != null) {
			if(Nightmare.instance.moduleManager.getModuleByName("Blur").isToggled() && Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("Blur"), "TargetHUD").getValBoolean()) {
				BlurUtils.drawBlurRect(x - 2, y - 2, x + 130, y + 34);
			}
			Gui.drawRect(x - 2, y - 2, x + 130, y + 34, ColorUtils.getBackgroundColor());
			mc.getTextureManager().bindTexture(((AbstractClientPlayer) target).getLocationSkin());
	        GL11.glPushMatrix();
	        GL11.glColor4f(1, 1, 1, 1);
	        GL11.glScaled(4, 4, 4);
	        drawScaledCustomSizeModalRect((float) ((x + 1.5) / 4) - (float)0.4, (float) ((y + 1.5) / 4) - (float) 0.4, 8.0f, (float) 8, 8, 8, 8, 8, 64.0f, 64.0f);
	        GL11.glScaled(1, 1, 1);
	        GL11.glPopMatrix();
	        Fonts.REGULAR.REGULAR_20.REGULAR_20.drawString(target.getName(), x + 35, y + 2.5, -1, true);
	        Fonts.ICON.ICON_16.ICON_16.drawString("D", x + 35, y + 15, -1);
	        Gui.drawRect(x + 45, y + 13, x + (int) (hp * 4) + 46, y + 19, hpColor);
	        Fonts.ICON.ICON_16.ICON_16.drawString("E", x + 35, y + 25, -1);
	        Gui.drawRect(x + 45, y + 23, x + (int) (armor * 4) + 46, y + 29, new Color(100, 160, 215).getRGB());
		}
	}
	
	private EntityLivingBase getClosest(double range) {
		double dist = range;
		EntityLivingBase target = null;
		for (Object object : mc.theWorld.loadedEntityList) {
			Entity entity = (Entity) object;
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase player = (EntityLivingBase) entity;
				if (canAttack(player)) {
					double currentDist = mc.thePlayer.getDistanceToEntity(player);
					if (currentDist <= dist) {
						dist = currentDist;
						target = player;
					}
				}
			}
		}
		return target;
	}
	
	private boolean canAttack(EntityLivingBase player) {

		if(player instanceof EntityAnimal || player instanceof EntitySquid || player instanceof EntityMob || player instanceof EntityVillager || player instanceof EntityArmorStand || player instanceof EntityBat || player.getDisplayName().getFormattedText().contains("[NPC]")) {
			return false;
		}

		return player != mc.thePlayer && player.isEntityAlive() && mc.thePlayer.getDistanceToEntity(player) <= (float) Nightmare.instance.settingsManager.getSettingByName(this, "Distance").getValDouble();
	}
	
    protected NetworkPlayerInfo getPlayerInfo()
    {
        if (this.playerInfo == null)
        {
            this.playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
        }

        return this.playerInfo;
    }

    public ResourceLocation getLocationSkin()
    {
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo == null ? DefaultPlayerSkin.getDefaultSkin(target.getUniqueID()) : networkplayerinfo.getLocationSkin();
    }
    
    public static void drawScaledCustomSizeModalRect(float x, float y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight)
    {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + (float)vHeight) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + (float)uWidth) * f), (double)((v + (float)vHeight) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + (float)uWidth) * f), (double)(v * f1)).endVertex();
        worldrenderer.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
        tessellator.draw();
    }
}
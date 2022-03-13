package nightmare.module.render;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventRenderGUI;
import nightmare.fonts.impl.Fonts;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.ChatUtils;
import nightmare.utils.ColorUtils;
import nightmare.utils.ScreenUtils;

public class HUD extends Module{
	
	protected float zLevelFloat;
	private int potionOffset;
	
	public HUD() {
		super("HUD", 0, Category.RENDER);
		
		ArrayList<String> options = new ArrayList<>();
		
		options.add("TopLeft");
		options.add("Middle");
		options.add("DownRight");
		
		Nightmare.instance.settingsManager.rSetting(new Setting("ArmorStatus", this, true));	
		Nightmare.instance.settingsManager.rSetting(new Setting("ClientInfo", this, true));	
		Nightmare.instance.settingsManager.rSetting(new Setting("PotionStatus", this, true));	
		Nightmare.instance.settingsManager.rSetting(new Setting("Position", this, "TopLeft", options));
		Nightmare.instance.settingsManager.rSetting(new Setting("Red", this, 0, 0, 255, true));
		Nightmare.instance.settingsManager.rSetting(new Setting("Green", this, 210, 0, 255, true));
		Nightmare.instance.settingsManager.rSetting(new Setting("Blue", this, 255, 0, 255, true));
	}
	
	@EventTarget
	public void onRender(EventRenderGUI event) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		
		String mode = Nightmare.instance.settingsManager.getSettingByName(this, "Position").getValString();
		
		int potionX = 0;
		int potionY = 0;
		
		if(Nightmare.instance.settingsManager.getSettingByName(this, "ArmorStatus").getValBoolean()) {
			for(int i21 = 0; i21 < mc.thePlayer.inventory.armorInventory.length; i21++) {
				ItemStack is = mc.thePlayer.inventory.armorInventory[i21];
				this.renderArmorStatus(sr, i21, is);
			}
		}
		
		if(Nightmare.instance.settingsManager.getSettingByName(this, "ClientInfo").getValBoolean()) {
			this.renderClientInfo(3, 4);
		}
		
		if(Nightmare.instance.settingsManager.getSettingByName(this, "PotionStatus").getValBoolean()) {
			
			if(mode.equals("TopLeft")) {
				potionX = 3;
				potionY = 20;
			}else if(mode.equals("Middle")){
				potionX = 3;
				potionY = ScreenUtils.getScreenHeight() / 3;
			}else if(mode.equals("DownRight")) {
				potionX = ScreenUtils.getScreenWidth() - 110;
				potionY = ScreenUtils.getScreenHeight() - potionOffset + 20;
			}
			
			this.renderPotionStatus(potionX, potionY);
		}
	}
	
	public void renderClientInfo(int x, int y) {
		Fonts.REGULAR.REGULAR_20.REGULAR_20.drawString("N" + ChatUtils.WHITE + "ightmare v" + Nightmare.instance.getVersion(), x, y, ColorUtils.getClientColor(), true);
	}
	
	private void renderArmorStatus(ScaledResolution sr, int pos, ItemStack itemStack) {
		
		if(itemStack == null) {
			return;
		}
		
		int posX = 0;
		int posY = 0;
		int posXAdd = (-16 * pos) + 48;

		if(mc.thePlayer.isSurvival()) {
			if(mc.thePlayer.isInsideOfMaterial(Material.water)) {
				mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, sr.getScaledWidth() / 2 + 20 + posXAdd, (int) (sr.getScaledHeight() - 65));
			}else {
				mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, sr.getScaledWidth() / 2 + 20 + posXAdd, (int) (sr.getScaledHeight() - 55));
			}
			
		}
		else if(mc.thePlayer.isCreative()) {
			mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, sr.getScaledWidth() / 2 + 20 + posXAdd, (int) (sr.getScaledHeight() - 41));
		}
	}
	
	private void renderPotionStatus(int x, int y) {
		int offsetX = 21;
		int offsetY = 14;
		int i = 80;
		int i2 = 16;
		Collection<PotionEffect> collection = this.mc.thePlayer.getActivePotionEffects();
		FontRenderer fr = mc.fontRendererObj;
		
		if (!collection.isEmpty())
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();
            int l = 33;

            if (collection.size() > 5)
            {
                l = 132 / (collection.size() - 1);
            }

            for (PotionEffect potioneffect : this.mc.thePlayer.getActivePotionEffects())
            {
                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                
                if (potion.hasStatusIcon())
                {
                	mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                    int i1 = potion.getStatusIconIndex();
                    drawTexturedModalRect((x + offsetX) - 20, (y + i2) - offsetY, 0 + i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                }
                
                String s1 = I18n.format(potion.getName(), new Object[0]);
                if (potioneffect.getAmplifier() == 1)
                {
                    s1 = s1 + " " + I18n.format("enchantment.level.2", new Object[0]);
                }
                else if (potioneffect.getAmplifier() == 2)
                {
                    s1 = s1 + " " + I18n.format("enchantment.level.3", new Object[0]);
                }
                else if (potioneffect.getAmplifier() == 3)
                {
                    s1 = s1 + " " + I18n.format("enchantment.level.4", new Object[0]);
                }
                
        		fr.drawString(s1, x + offsetX, (y + i2) - offsetY, 16777215, true);
                String s = Potion.getDurationString(potioneffect);
        		fr.drawString(s, x + offsetX, (y + i2 + 10) - offsetY, 8355711, true);
                i2 += l;
                potionOffset = i2;
            }
        }
	}
	
    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)(x + 0), (double)(y + height), (double)this.zLevelFloat).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), (double)this.zLevelFloat).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + 0), (double)this.zLevelFloat).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        worldrenderer.pos((double)(x + 0), (double)(y + 0), (double)this.zLevelFloat).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }
}

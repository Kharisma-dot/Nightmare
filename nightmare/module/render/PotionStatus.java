package nightmare.module.render;

import java.util.Collection;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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

public class PotionStatus extends Module{

	protected float zLevelFloat;
	
	public PotionStatus() {
		super("PotionStatus", 0, Category.RENDER);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("X", this, 20, 0, 5000, true));
		Nightmare.instance.settingsManager.rSetting(new Setting("Y", this, 20, 0, 5000, true));
	}
	
	@EventTarget
	public void onRender(EventRenderGUI event) {
		int offsetX = 21;
		int offsetY = 14;
		int i = 80;
		int i2 = 16;
		int x = (int) Nightmare.instance.settingsManager.getSettingByName(this, "X").getValDouble();
		int y = (int) Nightmare.instance.settingsManager.getSettingByName(this, "Y").getValDouble();
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
            }
        }
		
		if(mc.currentScreen instanceof GuiHudEditor) {
			Gui.drawRect(x, y, x + 110, y - 15, ColorUtils.getClientColor());
	        Fonts.REGULAR.REGULAR_23.REGULAR_23.drawString("PotionStatus", x + 5, y - 11, -1, false);
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

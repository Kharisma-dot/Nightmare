package nightmare.module.render;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventRenderGUI;
import nightmare.fonts.impl.Fonts;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.ColorUtils;
import nightmare.utils.render.BlurUtils;

public class ActiveMods extends Module{

	private FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	
	public ActiveMods() {
		super("ActiveMods", 0, Category.RENDER);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("BackGround", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("VanillaFont", this, false));
	}
	
	@EventTarget
	public void onRender(EventRenderGUI event) {
        final ArrayList<Module> enabledMods = new ArrayList<Module>();
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int moduleY = 0;
        boolean vanilla = Nightmare.instance.settingsManager.getSettingByName(this, "VanillaFont").getValBoolean();
        
        for (final Module i : Nightmare.instance.moduleManager.getModules()) {
            if (i.isToggled()) {
                enabledMods.add(i);
            }
        }
        
        if(vanilla) {
            enabledMods.sort((m1, m2) ->  fr.getStringWidth(m2.getDisplayName()) - fr.getStringWidth(m1.getDisplayName()));
        }else {
            enabledMods.sort((m1, m2) ->  Fonts.REGULAR.REGULAR_20.REGULAR_20.stringWidth(m2.getDisplayName()) - Fonts.REGULAR.REGULAR_20.REGULAR_20.stringWidth(m1.getDisplayName()));
        }
        
        for (final Module m : enabledMods) {
            if (m.visible) {
            	
            	if (Nightmare.instance.settingsManager.getSettingByName(this, "BackGround").getValBoolean()) {
            		if(vanilla) {
                		Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.getDisplayName()) - 6, moduleY * (fr.FONT_HEIGHT + 2), sr.getScaledWidth(), 2 + fr.FONT_HEIGHT + moduleY * (fr.FONT_HEIGHT + 2), ColorUtils.getBackgroundColor());
            		}else {
                		Gui.drawRect(sr.getScaledWidth() - Fonts.REGULAR.REGULAR_20.REGULAR_20.stringWidth(m.getDisplayName()) - 6, moduleY * (fr.FONT_HEIGHT + 2), sr.getScaledWidth(), 2 + fr.FONT_HEIGHT + moduleY * (fr.FONT_HEIGHT + 2), ColorUtils.getBackgroundColor());
            		}
            	}
            	
            	if(vanilla) {
                	fr.drawString(m.getDisplayName(), sr.getScaledWidth() - fr.getStringWidth(m.getDisplayName()) - 4, 2 + moduleY * (fr.FONT_HEIGHT + 2), ColorUtils.getClientColor(), true);
            	}else {
                	Fonts.REGULAR.REGULAR_20.REGULAR_20.drawString(m.getDisplayName(), sr.getScaledWidth() - Fonts.REGULAR.REGULAR_20.REGULAR_20.stringWidth(m.getDisplayName()) - 4, 2 + moduleY * (fr.FONT_HEIGHT + 2), ColorUtils.getClientColor(), true);
            	}
            	
            	moduleY++;
            }
        }
	}
}
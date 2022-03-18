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

	public HUD() {
		super("HUD", 0, Category.RENDER);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("ArmorStatus", this, true));	
		Nightmare.instance.settingsManager.rSetting(new Setting("ClientInfo", this, true));
		Nightmare.instance.settingsManager.rSetting(new Setting("Notification", this, true));
		Nightmare.instance.settingsManager.rSetting(new Setting("Scoreboard", this, true));
		Nightmare.instance.settingsManager.rSetting(new Setting("X", this, 20, 0, 5000, true));
		Nightmare.instance.settingsManager.rSetting(new Setting("Y", this, 20, 0, 5000, true));
		Nightmare.instance.settingsManager.rSetting(new Setting("Red", this, 0, 0, 255, true));
		Nightmare.instance.settingsManager.rSetting(new Setting("Green", this, 210, 0, 255, true));
		Nightmare.instance.settingsManager.rSetting(new Setting("Blue", this, 255, 0, 255, true));
	}
	
	@EventTarget
	public void onRender(EventRenderGUI event) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		
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
}

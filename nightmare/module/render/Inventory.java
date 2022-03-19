package nightmare.module.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventRenderGUI;
import nightmare.fonts.impl.Fonts;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.ColorUtils;
import nightmare.utils.render.BlurUtils;

public class Inventory extends Module{

	public Inventory() {
		super("Inventory", 0, Category.RENDER);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("X", this, 20, 0, 5000, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Y", this, 20, 0, 5000, false));
	}

	@EventTarget
	public void onRender(EventRenderGUI event) {
		int x = (int) Nightmare.instance.settingsManager.getSettingByName(this, "X").getValDouble();
		int y = (int) Nightmare.instance.settingsManager.getSettingByName(this, "Y").getValDouble();
        int startX = x + 2;
        int startY = y + 3;
        int curIndex = 0;

        if(Nightmare.instance.moduleManager.getModuleByName("Blur").isToggled() && Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("Blur"), "Inventory").getValBoolean()) {
        	BlurUtils.drawBlurRect(x, y, x + 185, y + 65);
        }
        
        Gui.drawRect(x, y - 15, x + 185, y, ColorUtils.getClientColor());
        Gui.drawRect(x, y, x + 185, y + 65, ColorUtils.getBackgroundColor());
        Fonts.REGULAR.REGULAR_23.REGULAR_23.drawString("Inventory", x + 5, y - 11, -1, false);
        
        for(int i = 9; i < 36; ++i) {
            ItemStack slot = mc.thePlayer.inventory.mainInventory[i];
            if(slot == null) {
                startX += 20;
                curIndex += 1;

                if(curIndex > 8) {
                    curIndex = 0;
                    startY += 20;
                    startX = x + 2;
                }

                continue;
            }

            this.drawItemStack(slot, startX, startY);
            startX += 20;
            curIndex += 1;
            if(curIndex > 8) {
                curIndex = 0;
                startY += 20;
                startX = x + 2;
            }
        }
	}
	
    private void drawItemStack(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        mc.getRenderItem().zLevel = -150.0F;
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        mc.getRenderItem().renderItemIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y, null);
        mc.getRenderItem().zLevel = 0.0F;
        GlStateManager.enableAlpha();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}

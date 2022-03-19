package nightmare.module.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventRender3D;
import nightmare.fonts.impl.Fonts;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.ColorUtils;
import nightmare.utils.WorldUtils;
import nightmare.utils.render.RenderUtils;

public class NameTags extends Module{

	private FontRenderer fr = mc.fontRendererObj;
	
	public NameTags() {
		super("NameTags", 0, Category.RENDER);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("Armor", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Background", this, true));
		Nightmare.instance.settingsManager.rSetting(new Setting("VanillaFont", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Scale", this, 0.6, 0.1, 1, false));
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
        for (EntityPlayer entity : WorldUtils.getLivingPlayers()) {
            final double yOffset = entity.isSneaking() ? -0.25 : 0.0;

            final double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - this.mc.getRenderManager().getRenderPosX();
            final double posY = (entity.lastTickPosY + yOffset) + ((entity.posY + yOffset) - (entity.lastTickPosY + yOffset)) * mc.timer.renderPartialTicks - this.mc.getRenderManager().getRenderPosY();
            final double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - this.mc.getRenderManager().getRenderPosZ();

            mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);

            this.renderNameTag(entity, posX, posY, posZ, event.getPartialTicks());
        }
	}
	
    private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        double tempY = y + 0.7D;

        Entity camera = this.mc.getRenderViewEntity();
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        
        boolean vanilla = Nightmare.instance.settingsManager.getSettingByName(this, "VanillaFont").getValBoolean();
        
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);


        double distance = camera.getDistance(x + this.mc.getRenderManager().viewerPosX, y + this.mc.getRenderManager().viewerPosY, z + this.mc.getRenderManager().viewerPosZ);

        float width;

        if(vanilla) {
        	width = fr.getStringWidth(player.getDisplayName().getFormattedText()) / 2;
        }else {
        	width = Fonts.REGULAR.REGULAR_20.REGULAR_20.stringWidth(player.getDisplayName().getFormattedText()) / 2;
        }
        
        double scale = (double) (0.004F * Nightmare.instance.settingsManager.getSettingByName(this, "Scale").getValDouble()) * distance;

        if (scale < 0.01)
            scale = 0.01;

        GlStateManager.pushMatrix();

        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0F, -1500000.0F);

        GlStateManager.disableLighting();

        GlStateManager.translate((float) x, (float) tempY + 1.4F, (float) z);
        GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        float var10001 = this.mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;
        GlStateManager.rotate(this.mc.getRenderManager().playerViewX, var10001, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
        if(Nightmare.instance.settingsManager.getSettingByName(this, "Background").getValBoolean()) {
        	
        	if(vanilla) {
                RenderUtils.drawRect(-width - 2, -(fr.FONT_HEIGHT + 3), (float) width + 2.0F, 2.0F, ColorUtils.getBackgroundColor());
        	}else {
                RenderUtils.drawRect(-width - 2, -(Fonts.REGULAR.REGULAR_20.REGULAR_20.getHeight() + 3), (float) width + 2.0F, 2.0F, ColorUtils.getBackgroundColor());
        	}
        }
        
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        
        GL11.glDepthMask(false);
        
        if(vanilla) {
            fr.drawString(player.getDisplayName().getFormattedText(), (int) -width, -(fr.FONT_HEIGHT), this.getDisplayColour(player));
        }else {
            Fonts.REGULAR.REGULAR_20.REGULAR_20.drawString(player.getDisplayName().getFormattedText(), -width, -(Fonts.REGULAR.REGULAR_20.REGULAR_20.getHeight()), this.getDisplayColour(player));
        }
        
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDepthMask(true);

        if (Nightmare.instance.settingsManager.getSettingByName(this, "Armor").getValBoolean()) {
            this.renderArmor(player);
            
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
        }

        GlStateManager.disableLighting();

        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;

        GlStateManager.doPolygonOffset(1.0F, 1500000.0F);
        GlStateManager.disablePolygonOffset();

        GlStateManager.popMatrix();
    }
    
    private void renderArmor(EntityPlayer player) {
        int xOffset = 0;

        int index;
        ItemStack stack;
        for (index = 3; index >= 0; --index) {
            stack = player.inventory.armorInventory[index];
            if (stack != null) {
                xOffset -= 8;
            }
        }

        if (player.getCurrentEquippedItem() != null) {
            xOffset -= 8;
            ItemStack var27 = player.getCurrentEquippedItem().copy();
            if (var27.hasEffect() && (var27.getItem() instanceof ItemTool || var27.getItem() instanceof ItemArmor)) {
                var27.stackSize = 1;
            }

            this.renderItemStack(var27, xOffset, -26);
            xOffset += 16;
        }

        for (index = 3; index >= 0; --index) {
            stack = player.inventory.armorInventory[index];
            if (stack != null) {
                ItemStack armourStack = stack.copy();
                if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
                    armourStack.stackSize = 1;
                }

                this.renderItemStack(armourStack, xOffset, -26);
                xOffset += 16;
            }
        }
    }
    
    private void renderItemStack(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();

        GlStateManager.disableAlpha();
        this.mc.getRenderItem().zLevel = -150.0F;

        GlStateManager.disableCull();

        this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, stack, x, y);

        GlStateManager.enableCull();

        this.mc.getRenderItem().zLevel = 0;

        GlStateManager.disableBlend();

        GlStateManager.scale(0.5F, 0.5F, 0.5F);

        GlStateManager.disableDepth();
        GlStateManager.disableLighting();

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();

        GlStateManager.scale(2.0F, 2.0F, 2.0F);

        GlStateManager.enableAlpha();

        GlStateManager.popMatrix();
    }
    
    private int getDisplayColour(EntityPlayer player) {
        int colour = new Color(0xFFFFFF).getRGB();

        if (player.isInvisible()) {
            colour = -1113785;
        }

        return colour;
    }
    
    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double) delta;
    }
}

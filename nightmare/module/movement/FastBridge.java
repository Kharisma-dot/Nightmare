package nightmare.module.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class FastBridge extends Module{

	public FastBridge() {
		super("FastBridge", 0, Category.MOVEMENT);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("SmartCheck", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("HeldItem", this, false));
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		ItemStack i = mc.thePlayer.getCurrentEquippedItem();
		BlockPos bP = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.5D, mc.thePlayer.posZ);
		
		if(this.isToggled()) {
			if(Nightmare.instance.settingsManager.getSettingByName(this, "HeldItem").getValBoolean()) {
				if(i == null) {
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
				}
			}
			
			if(Nightmare.instance.settingsManager.getSettingByName(this, "SmartCheck").getValBoolean()) {
				if(!mc.thePlayer.onGround) {
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
				}
				if(mc.thePlayer.isSprinting()) {
					return;
				}
			}
			
			if(Nightmare.instance.settingsManager.getSettingByName(this, "HeldItem").getValBoolean() ? (i != null && i.getItem() instanceof ItemBlock) : true) {
				if(mc.thePlayer.onGround) {
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
					if(!(mc.currentScreen instanceof Gui)) {
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()));
					}
					
					if(mc.theWorld.getBlockState(bP).getBlock() == Blocks.air) {
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
					}
				}
			}
		}
	}
	
	@Override
	public void onDisable() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
	}
}
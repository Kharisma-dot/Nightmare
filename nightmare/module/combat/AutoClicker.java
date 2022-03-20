package nightmare.module.combat;

import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C02PacketUseEntity;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventPreMotionUpdate;
import nightmare.event.impl.EventTick;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.TimerUtils;

public class AutoClicker extends Module{

	private TimerUtils timer = new TimerUtils();
	
	public AutoClicker() {
		super("AutoClicker", 0, Category.COMBAT);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("MinCPS", this, 12, 1, 20, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("MaxCPS", this, 15, 1, 20, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Weapons Only", this, false));
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		
		if(Nightmare.instance.settingsManager.getSettingByName(this, "MinCPS").getValDouble() > Nightmare.instance.settingsManager.getSettingByName(this, "MaxCPS").getValDouble() - 1) {
			Nightmare.instance.settingsManager.getSettingByName(this, "MinCPS").setValDouble(Nightmare.instance.settingsManager.getSettingByName(this, "MaxCPS").getValDouble() - 0.1);
		}
		
		if (mc.gameSettings.keyBindAttack.isKeyDown() && (!Nightmare.instance.settingsManager.getSettingByName(this, "Weapons Only").getValBoolean() || mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemTool || mc.thePlayer.getHeldItem().getItem() instanceof ItemSword))) {
            if (timer.delay(1000 / ThreadLocalRandom.current().nextInt((int) Nightmare.instance.settingsManager.getSettingByName(this, "MinCPS").getValDouble(), (int) Nightmare.instance.settingsManager.getSettingByName(this, "MaxCPS").getValDouble() + 1))) {
            	KeyBinding.onTick(mc.gameSettings.keyBindAttack.getKeyCode());
                timer.reset();
            }
        }
	}
}

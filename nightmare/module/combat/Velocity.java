package nightmare.module.combat;

import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class Velocity extends Module{

	public Velocity() {
		super("Velocity", 0, Category.COMBAT);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("Horizontal", this, 90, 0, 100, true));
		Nightmare.instance.settingsManager.rSetting(new Setting("Vertical", this, 90, 0, 100, true));
		Nightmare.instance.settingsManager.rSetting(new Setting("Chance", this, 50, 0, 100, true));
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {

		if (mc.thePlayer == null) {
			return;
		}
		
		float horizontal = (float) Nightmare.instance.settingsManager.getSettingByName(this, "Horizontal").getValDouble();
		float vertical = (float) Nightmare.instance.settingsManager.getSettingByName(this, "Vertical").getValDouble();
		
		if (mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime && mc.thePlayer.maxHurtTime > 0) {
			if (Math.random() <= Nightmare.instance.settingsManager.getSettingByName(this, "Chance").getValDouble() / 100) {
				mc.thePlayer.motionX *= (float) horizontal / 100;
				mc.thePlayer.motionY *= (float) vertical / 100;
				mc.thePlayer.motionZ *= (float) horizontal / 100;
			}else {
				mc.thePlayer.motionX = mc.thePlayer.motionX;
				mc.thePlayer.motionY =  mc.thePlayer.motionY;
				mc.thePlayer.motionZ =  mc.thePlayer.motionZ;
			}
		}
	}
}
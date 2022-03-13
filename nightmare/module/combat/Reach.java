package nightmare.module.combat;

import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventTick;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.ChatUtils;

public class Reach extends Module{

	public Reach() {
		super("Reach", 0, Category.COMBAT);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("Range", this, 3.5D, 3.0D, 6.0D, false));
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		this.setDisplayName("Reach " + ChatUtils.GRAY + Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("Reach"), "Range").getValDouble());
	}
	
	public static double getMaxRange(){
        double range = Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("Reach"), "Range").getValDouble();
        double buildRange = range = 4.5;

        if(range > buildRange) {
        	return range;
        }else {
        	return buildRange;
        }
	}
}

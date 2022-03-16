package nightmare.module.misc;

import nightmare.Nightmare;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class FPSBoost extends Module{

	public FPSBoost() {
		super("FPSBoost", 0, Category.MISC);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("HideArmorStand", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("HideBat", this, false));
	}

}

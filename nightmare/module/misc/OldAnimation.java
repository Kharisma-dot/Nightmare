package nightmare.module.misc;

import nightmare.Nightmare;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class OldAnimation extends Module{

	public OldAnimation() {
		super("OldAnimation", 0, Category.MISC);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("BlockHitting", this, true));
		Nightmare.instance.settingsManager.rSetting(new Setting("Health", this, true));
	}

}

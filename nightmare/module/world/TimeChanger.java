package nightmare.module.world;

import nightmare.Nightmare;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class TimeChanger extends Module{

	public TimeChanger() {
		super("TimeChanger", 0, Category.WORLD);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("Time", this, 0.0D, 0.0D, 15000.0D, true));
	}
}

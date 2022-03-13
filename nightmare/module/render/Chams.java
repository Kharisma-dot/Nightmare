package nightmare.module.render;

import nightmare.Nightmare;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class Chams extends Module{

	public Chams() {
		super("Chams", 0, Category.RENDER);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("Player", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Chest", this, false));
	}

}

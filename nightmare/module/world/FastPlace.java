package nightmare.module.world;

import nightmare.Nightmare;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class FastPlace extends Module{

	public FastPlace() {
		super("FastPlace", 0, Category.WORLD);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("Speed", this, 2, 0, 4, true));
	}

}
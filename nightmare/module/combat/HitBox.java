package nightmare.module.combat;

import nightmare.Nightmare;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class HitBox extends Module{

	public HitBox() {
		super("HitBox", 0, Category.COMBAT);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("EntityBox", this, 0.1, 0.1, 1.0, false));
	}

}
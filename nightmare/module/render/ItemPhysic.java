package nightmare.module.render;

import nightmare.Nightmare;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class ItemPhysic extends Module{

	public ItemPhysic() {
		super("ItemPhysic", 0, Category.RENDER);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("Speed", this, 1, 0.5, 4, false));
	}

}

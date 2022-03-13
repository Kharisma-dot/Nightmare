package nightmare.module.render;

import nightmare.Nightmare;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class Chat extends Module{

	public Chat() {
		super("Chat", 0, Category.RENDER);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("BetterChat", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("ClearChat", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("CompactChat", this, false));
	}

}

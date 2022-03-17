package nightmare.module.render;

import net.minecraft.src.Config;
import nightmare.Nightmare;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class Blur extends Module{

	public Blur() {
		super("Blur", 0, Category.RENDER);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("ClickGUI", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Container", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Inventory", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Scoreboard", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("TargetHUD", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Radius", this, 20, 1, 80, true));
	}
	
	@Override
	public void onEnable() {
		if(Config.isFastRender()) {
			this.setToggled(false);
		}
	}
}
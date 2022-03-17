package nightmare.module.render;

import org.lwjgl.input.Keyboard;

import nightmare.Nightmare;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class ClickGUI extends Module{

	public ClickGUI() {
		super("ClickGUI", Keyboard.KEY_RSHIFT, Category.RENDER);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("VanillaFont", this, false));
	}

    @Override
    public void onEnable() {
        super.onEnable();
    	mc.displayGuiScreen(Nightmare.instance.clickGUI);
        this.setToggled(false);
    }
}

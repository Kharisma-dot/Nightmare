package nightmare.module.render;

import org.lwjgl.input.Keyboard;

import nightmare.Nightmare;
import nightmare.module.Category;
import nightmare.module.Module;

public class ClickGUI extends Module{

	public ClickGUI() {
		super("ClickGUI", Keyboard.KEY_RSHIFT, Category.RENDER);
	}

    @Override
    public void onEnable() {
        super.onEnable();
    	mc.displayGuiScreen(Nightmare.instance.clickGUI);
        this.setToggled(false);
    }
}

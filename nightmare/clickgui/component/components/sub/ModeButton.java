package nightmare.clickgui.component.components.sub;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import nightmare.Nightmare;
import nightmare.clickgui.component.Component;
import nightmare.clickgui.component.components.Button;
import nightmare.fonts.impl.Fonts;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.ColorUtils;

public class ModeButton extends Component {

	private boolean hovered;
	private Button parent;
	private Setting set;
	private int offset;
	private int x;
	private int y;
	private Module mod;

	private int modeIndex;
	
	private FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	
	public ModeButton(Setting set, Button button, Module mod, int offset) {
		this.set = set;
		this.parent = button;
		this.mod = mod;
		this.x = button.frame.getX() + button.frame.getWidth();
		this.y = button.frame.getY() + button.offset;
		this.offset = offset;
		this.modeIndex = 0;
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void renderComponent() {
		Gui.drawRect(parent.frame.getX(), parent.frame.getY() + offset, parent.frame.getX() + (parent.frame.getWidth() * 1), parent.frame.getY() + offset + 18, ColorUtils.getBackgroundColor());
		
		boolean vanilla = Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("ClickGUI"), "VanillaFont").getValBoolean();
		
		if(vanilla) {
			fr.drawString("Mode: " + set.getValString(), (parent.frame.getX() + 7), (parent.frame.getY() + offset + 2) + 5, -1);
		}else {
			Fonts.REGULAR.REGULAR_20.REGULAR_20.drawString("Mode: " + set.getValString(), (parent.frame.getX() + 7), (parent.frame.getY() + offset + 2) + 5, -1);
		}
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.frame.getY() + offset;
		this.x = parent.frame.getX();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			int maxIndex = set.getOptions().size();

			if(modeIndex + 1 >= maxIndex)
				modeIndex = 0;
			else
				modeIndex++;

			set.setValString(set.getOptions().get(modeIndex));
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x && x < this.x + 88 && y > this.y && y < this.y + 18) {
			return true;
		}
		return false;
	}
}

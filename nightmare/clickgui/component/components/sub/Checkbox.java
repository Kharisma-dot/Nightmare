package nightmare.clickgui.component.components.sub;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import nightmare.Nightmare;
import nightmare.clickgui.component.Component;
import nightmare.clickgui.component.components.Button;
import nightmare.fonts.impl.Fonts;
import nightmare.settings.Setting;
import nightmare.utils.ColorUtils;

public class Checkbox extends Component {

	private boolean hovered;
	private Setting op;
	private Button parent;
	private int offset;
	private int x;
	private int y;
	
	private FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	
	public Checkbox(Setting option, Button button, int offset) {
		this.op = option;
		this.parent = button;
		this.x = button.frame.getX() + button.frame.getWidth();
		this.y = button.frame.getY() + button.offset;
		this.offset = offset;
	}

	@Override
	public void renderComponent() {
		Gui.drawRect(parent.frame.getX(), parent.frame.getY() + offset, parent.frame.getX() + (parent.frame.getWidth() * 1), parent.frame.getY() + offset + 18, ColorUtils.getBackgroundColor());
		
		boolean vanilla = Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("ClickGUI"), "VanillaFont").getValBoolean();
		
		if(vanilla) {
			fr.drawString(this.op.getName(), (parent.frame.getX() + 17), (parent.frame.getY() + offset + 7), -1);
		}else {
			Fonts.REGULAR.REGULAR_20.REGULAR_20.drawString(this.op.getName(), (parent.frame.getX() + 17), (parent.frame.getY() + offset + 7), -1);
		}
		
		Gui.drawRect(parent.frame.getX() + 4, parent.frame.getY() + offset + 5, parent.frame.getX() + 14, parent.frame.getY() + offset + 15, ColorUtils.getClientColor());
		
		if(op.getValBoolean()) {
			Fonts.ICON.ICON_20.ICON_20.drawString("A", parent.frame.getX() + 4, parent.frame.getY() + offset + 8, -1);
		}
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
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
			this.op.setValBoolean(!op.getValBoolean());;
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x && x < this.x + 88 && y > this.y && y < this.y + 18) {
			return true;
		}
		return false;
	}
}
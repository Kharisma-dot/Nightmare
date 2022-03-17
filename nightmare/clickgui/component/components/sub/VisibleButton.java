package nightmare.clickgui.component.components.sub;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import nightmare.Nightmare;
import nightmare.clickgui.component.Component;
import nightmare.clickgui.component.components.Button;
import nightmare.fonts.impl.Fonts;
import nightmare.module.Module;
import nightmare.utils.ColorUtils;

public class VisibleButton extends Component {

	private boolean hovered;
	private Button parent;
	private int offset;
	private int x;
	private int y;
	private Module mod;
	
	private FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	
	public VisibleButton(Button button, Module mod, int offset) {
		this.parent = button;
		this.mod = mod;
		this.x = button.frame.getX() + button.frame.getWidth();
		this.y = button.frame.getY() + button.offset;
		this.offset = offset;
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void renderComponent() {
		
		boolean vanilla = Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("ClickGUI"), "VanillaFont").getValBoolean();
		
		Gui.drawRect(parent.frame.getX(), parent.frame.getY() + offset, parent.frame.getX() + (parent.frame.getWidth() * 1), parent.frame.getY() + offset + 18, ColorUtils.getBackgroundColor());
		
		if(vanilla) {
			fr.drawString("Visible: " + mod.visible, (parent.frame.getX() + 7), (parent.frame.getY() + offset + 1) + 5, -1);
		}else {
			Fonts.REGULAR.REGULAR_20.REGULAR_20.drawString("Visible: " + mod.visible, (parent.frame.getX() + 7), (parent.frame.getY() + offset + 1) + 5, -1);
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
			mod.visible = (!mod.visible);
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x && x < this.x + 99 && y > this.y && y < this.y + 18) {
			return true;
		}
		return false;
	}
}

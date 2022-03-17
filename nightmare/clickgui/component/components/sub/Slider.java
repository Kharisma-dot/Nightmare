package nightmare.clickgui.component.components.sub;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import nightmare.Nightmare;
import nightmare.clickgui.component.Component;
import nightmare.clickgui.component.components.Button;
import nightmare.fonts.impl.Fonts;
import nightmare.settings.Setting;
import nightmare.utils.ColorUtils;

public class Slider extends Component {

	private boolean hovered;

	private Setting set;
	private Button parent;
	private int offset;
	private int x;
	private int y;
	private boolean dragging = false;

	private double renderWidth;
	
	private FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	
	public Slider(Setting value, Button button, int offset) {
		this.set = value;
		this.parent = button;
		this.x = button.frame.getX() + button.frame.getWidth();
		this.y = button.frame.getY() + button.offset;
		this.offset = offset;
	}
	
	@Override
	public void renderComponent() {
		
		boolean vanilla = Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("ClickGUI"), "VanillaFont").getValBoolean();
		
		Gui.drawRect(parent.frame.getX(), parent.frame.getY() + offset, parent.frame.getX() + parent.frame.getWidth(), parent.frame.getY() + offset + 18, ColorUtils.getBackgroundColor());
		final int drag = (int)(this.set.getValDouble() / this.set.getMax() * this.parent.frame.getWidth());
		Gui.drawRect(parent.frame.getX(), parent.frame.getY() + offset + 2, parent.frame.getX() + (int) renderWidth, parent.frame.getY() + offset + 18, ColorUtils.getClientColor());
		
		if(vanilla) {
			fr.drawString(this.set.getName() + ": " + this.set.getValDouble() , (parent.frame.getX() + 5), (parent.frame.getY() + offset + 2)  + 5, -1);
		}else {
			Fonts.REGULAR.REGULAR_20.REGULAR_20.drawString(this.set.getName() + ": " + this.set.getValDouble() , (parent.frame.getX() + 5), (parent.frame.getY() + offset + 2)  + 5, -1);
		}
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButtonD(mouseX, mouseY) || isMouseOnButtonI(mouseX, mouseY);
		this.y = parent.frame.getY() + offset;
		this.x = parent.frame.getX();
		
		double diff = Math.min(99, Math.max(0, mouseX - this.x));

		double min = set.getMin();
		double max = set.getMax();
		
		renderWidth = (99) * (set.getValDouble() - min) / (max - min);
		
		if (dragging) {
			if (diff == 0) {
				set.setValDouble(set.getMin());
			}
			else {
				double newValue = roundToPlace(((diff / 99) * (max - min) + min), 2);
				set.setValDouble(newValue);
			}
		}
	}
	
	private static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open) {
			dragging = true;
		}
		if(isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open) {
			dragging = true;
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		dragging = false;
	}
	
	public boolean isMouseOnButtonD(int x, int y) {
		if(x > this.x && x < this.x + (parent.frame.getWidth() / 2 + 1) && y > this.y && y < this.y + 18) {
			return true;
		}
		return false;
	}
	
	public boolean isMouseOnButtonI(int x, int y) {
		if(x > this.x + parent.frame.getWidth() / 2 && x < this.x + parent.frame.getWidth() && y > this.y && y < this.y + 18) {
			return true;
		}
		return false;
	}
}

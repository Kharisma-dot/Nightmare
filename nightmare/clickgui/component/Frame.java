package nightmare.clickgui.component;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import nightmare.Nightmare;
import nightmare.clickgui.component.components.Button;
import nightmare.fonts.impl.Fonts;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.utils.ColorUtils;
import nightmare.utils.render.BlurUtils;

public class Frame {

	public ArrayList<Component> components;
	public Category category;
	private boolean open;
	private int width;
	private int y;
	private int x;
	private int barHeight;
	public boolean isDragging;
	public int dragX;
	public int dragY;
	
	private FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	
	public Frame(Category cat) {
		this.components = new ArrayList<Component>();
		this.category = cat;
		this.width = 99;
		this.x = 20;
		this.y = 20;
		this.barHeight = 13;
		this.dragX = 0;
		this.open = true;
		this.isDragging = false;
		int tY = this.barHeight;

		for(Module mod : Nightmare.instance.moduleManager.getModulesInCategory(category)) {
			Button modButton = new Button(mod, this, tY);
			this.components.add(modButton);
			tY += 18;
		}
	}
	
	public ArrayList<Component> getComponents() {
		return components;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public void setDrag(boolean drag) {
		this.isDragging = drag;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public void renderFrame(FontRenderer fontRenderer) {
		
		Gui.drawRect(this.x, this.y - 4, this.x + this.width, this.y + this.barHeight, ColorUtils.getClientColor());
		
		boolean vanilla = Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("ClickGUI"), "VanillaFont").getValBoolean();
		
		if(vanilla) {
			fr.drawString(this.category.name().replace("COMBAT", "Combat").replace("MOVEMENT", "Movement").replace("RENDER", "Render").replace("PLAYER", "Player").replace("WORLD", "World").replace("MISC", "Misc"), this.x + 2, (this.y) + 2, -1);
			fr.drawString(this.open ? "-" : "+", this.x + 89, (this.y) + 1, -1);
		}else {
			Fonts.REGULAR.REGULAR_20.REGULAR_20.drawString(this.category.name().replace("COMBAT", "Combat").replace("MOVEMENT", "Movement").replace("RENDER", "Render").replace("PLAYER", "Player").replace("WORLD", "World").replace("MISC", "Misc"), this.x + 2, (this.y) + 2, -1);
			Fonts.REGULAR.REGULAR_20.REGULAR_20.drawString(this.open ? "-" : "+", this.x + 89, (this.y) + 1, -1);
		}

		if(this.open) {
			if(!this.components.isEmpty()) {
				for(Component component : components) {
					component.renderComponent();
				}
			}
		}
	}
	
	public void refresh() {
		int off = this.barHeight;
		for(Component comp : components) {
			comp.setOff(off);
			off += comp.getHeight();
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void updatePosition(int mouseX, int mouseY) {
		if(this.isDragging) {
			this.setX(mouseX - dragX);
			this.setY(mouseY - dragY);
		}
	}
	
	public boolean isWithinHeader(int x, int y) {
		if(x >= this.x && x <= this.x + this.width && y >= this.y - 4 && y <= this.y + this.barHeight) {
			return true;
		}
		return false;
	}
	
}

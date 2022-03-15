package nightmare.clickgui;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import nightmare.Nightmare;
import nightmare.clickgui.component.Component;
import nightmare.clickgui.component.Frame;
import nightmare.gui.GuiHudEditor;
import nightmare.module.Category;
import nightmare.utils.ScreenUtils;

public class ClickGUI extends GuiScreen {

	public static ArrayList<Frame> frames;
	
	public ClickGUI() {
		this.frames = new ArrayList<Frame>();
		int frameX = 20;
		for(Category category : Category.values()) {
			Frame frame = new Frame(category);
			frame.setX(frameX);
			frame.setY(25);
			frames.add(frame);
			frameX += frame.getWidth() + 20;
		}
	}
	
	@Override
	public void initGui() {	
        this.buttonList.add(new GuiButton(0, 5, ScreenUtils.getHeight() - 25, 120, 20, "Go to EditHUD"));
    	//Blur
    	if(Nightmare.instance.moduleManager.getModuleByName("Blur").isToggled() && Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("Blur"), "ClickGUI").getValBoolean()) {
    		if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
    			if (mc.entityRenderer.theShaderGroup != null) {
    				mc.entityRenderer.theShaderGroup.deleteShaderGroup();
    			}
    			mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
    		}
    	}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for(Frame frame : frames) {
			frame.renderFrame(this.fontRendererObj);
			frame.updatePosition(mouseX, mouseY);
			for(Component comp : frame.getComponents()) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
    
	@Override
    public void updateScreen() {
    	super.updateScreen();
        if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead)
        {
            mc.thePlayer.closeScreen();
        }
    }
    
	@Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		for(Frame frame : frames) {
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDrag(true);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
				frame.setOpen(!frame.isOpen());
			}
			if(frame.isOpen()) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
						component.mouseClicked(mouseX, mouseY, mouseButton);
					}
				}
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		for(Frame frame : frames) {
			if(frame.isOpen() && keyCode != 1) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
						component.keyTyped(typedChar, keyCode);
					}
				}
			}
		}
		if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
	}

	
	@Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {    
		for(Frame frame : frames) {
			frame.setDrag(false);
		}
		for(Frame frame : frames) {
			if(frame.isOpen()) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
						component.mouseReleased(mouseX, mouseY, state);
					}
				}
			}
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void onGuiClosed() {
		//Blur
		if (mc.entityRenderer.theShaderGroup != null) {
			mc.entityRenderer.theShaderGroup.deleteShaderGroup();
			mc.entityRenderer.theShaderGroup = null;
		}
	}
	
    @Override
    protected void actionPerformed(GuiButton button) {
    	if(button.id == 0) {
        	mc.displayGuiScreen(new GuiHudEditor());
    	}
    }
}
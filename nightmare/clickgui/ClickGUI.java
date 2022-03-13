package nightmare.clickgui;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import nightmare.Nightmare;
import nightmare.clickgui.component.Component;
import nightmare.clickgui.component.Frame;
import nightmare.module.Category;
import nightmare.utils.MouseUtils;

public class ClickGUI extends GuiScreen {

	public static ArrayList<Frame> frames;
	
	//Drag System
	private boolean draggingInventory = false;
	private int inventoryX;
	private int inventoryY;
	private int dragInventoryX;
	private int dragInventoryY;
	
	private boolean draggingTargetHUD = false;
	private int targethudX;
	private int targethudY;
	private int dragTargethudX;
	private int dragTargethudY;
	
	private boolean draggingPotionStatus = false;
	private int potionstatusX;
	private int potionstatusY;
	private int dragpotionstatusX;
	private int dragpotionstatusY;
	
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
		//Drag System
    	this.draggingInventory = false;
    	this.draggingTargetHUD = false;
    	this.draggingPotionStatus = false;
    	
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
		
		//Drag System
    	if(this.draggingInventory == true) {
    		Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("Inventory"), "X").setValDouble(mouseX + this.dragInventoryX);
    		Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("Inventory"), "Y").setValDouble(mouseY + this.dragInventoryY);
    	}

    	if(this.draggingTargetHUD == true) {
    		Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("TargetHUD"), "X").setValDouble(mouseX + this.dragTargethudX);
    		Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("TargetHUD"), "Y").setValDouble(mouseY + this.dragTargethudY);
    	}
    	
    	if(this.draggingPotionStatus == true) {
    		Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("PotionStatus"), "X").setValDouble(mouseX + this.dragpotionstatusX);
    		Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("PotionStatus"), "Y").setValDouble(mouseY + this.dragpotionstatusY);
    	}

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
		
		//Drag System
        this.inventoryX = (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("Inventory"), "X").getValDouble();
        this.inventoryY = (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("Inventory"), "Y").getValDouble();
        
        this.targethudX = (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("TargetHUD"), "X").getValDouble();
        this.targethudY = (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("TargetHUD"), "Y").getValDouble();
        
        this.potionstatusX = (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("PotionStatus"), "X").getValDouble();
        this.potionstatusY = (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("PotionStatus"), "Y").getValDouble();
        
        if(MouseUtils.isInside(mouseX, mouseY, inventoryX, inventoryY - 15, inventoryX + 185, inventoryY) && Nightmare.instance.moduleManager.getModuleByName("Inventory").isToggled() && mouseButton == 0) {
        	this.draggingInventory = true;
        	this.dragInventoryX = this.inventoryX - mouseX;
        	this.dragInventoryY = this.inventoryY - mouseY;
        }
        
        if(MouseUtils.isInside(mouseX, mouseY, targethudX - 2, targethudY - 17, targethudX + 130, targethudY - 2) && Nightmare.instance.moduleManager.getModuleByName("TargetHUD").isToggled() && mouseButton == 0) {
        	this.draggingTargetHUD = true;
        	this.dragTargethudX = this.targethudX - mouseX;
        	this.dragTargethudY = this.targethudY - mouseY;
        }
        
        if(MouseUtils.isInside(mouseX, mouseY, potionstatusX, potionstatusY - 15, potionstatusX + 110, potionstatusY) && Nightmare.instance.moduleManager.getModuleByName("PotionStatus").isToggled() && mouseButton == 0) {
        	this.draggingPotionStatus = true;
        	this.dragpotionstatusX = this.potionstatusX - mouseX;
        	this.dragpotionstatusY = this.potionstatusY - mouseY;
        }
        
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
		
		//Drag System
        this.draggingInventory = false;
        this.draggingTargetHUD = false;
        this.draggingPotionStatus = false;
        
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
}
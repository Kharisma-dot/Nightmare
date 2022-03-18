package nightmare.gui;

import java.io.IOException;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import nightmare.Nightmare;
import nightmare.fonts.impl.Fonts;
import nightmare.utils.ColorUtils;
import nightmare.utils.MouseUtils;
import nightmare.utils.ScreenUtils;

public class GuiHudEditor extends GuiScreen{
	
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
	
	private boolean draggingScoreboard = false;
	private int scoreboardX;
	private int scoreboardY;
	private int dragScoreboardX;
	private int dragScoreboardY;
	
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, 5, ScreenUtils.getHeight() - 25, 120, 20, "Go to ClickGUI"));
    	this.draggingInventory = false;
    	this.draggingTargetHUD = false;
    	this.draggingPotionStatus = false;
    	this.draggingScoreboard = false;
        super.initGui();
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	
    	if(Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("HUD"), "Scoreboard").getValBoolean()){
    		Gui.drawRect(GuiIngame.scoreboardX, GuiIngame.scoreboardY, GuiIngame.scoreboardX1, GuiIngame.scoreboardY - 15, ColorUtils.getClientColor());
            Fonts.REGULAR.REGULAR_23.REGULAR_23.drawString("Scoreboard", GuiIngame.scoreboardX + 5, GuiIngame.scoreboardY - 11, -1, false);
    	}
    	
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
    	
    	if(this.draggingScoreboard == true) {
    		Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("HUD"), "X").setValDouble(mouseX + this.dragScoreboardX);
    		Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("HUD"), "Y").setValDouble(mouseY + this.dragScoreboardY);
    	}
    	
    	super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.inventoryX = (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("Inventory"), "X").getValDouble();
        this.inventoryY = (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("Inventory"), "Y").getValDouble();
        
        this.targethudX = (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("TargetHUD"), "X").getValDouble();
        this.targethudY = (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("TargetHUD"), "Y").getValDouble();
        
        this.potionstatusX = (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("PotionStatus"), "X").getValDouble();
        this.potionstatusY = (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("PotionStatus"), "Y").getValDouble();
        
        this.scoreboardX = (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("HUD"), "X").getValDouble();
        this.scoreboardY = (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("HUD"), "Y").getValDouble();
        
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
        
        if(MouseUtils.isInside(mouseX, mouseY, GuiIngame.scoreboardX, GuiIngame.scoreboardY - 15, GuiIngame.scoreboardX1, GuiIngame.scoreboardY) && mouseButton == 0) {
        	this.draggingScoreboard = true;
        	this.dragScoreboardX = this.scoreboardX - mouseX;
        	this.dragScoreboardY = this.scoreboardY - mouseY;
        }
        
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.draggingInventory = false;
        this.draggingTargetHUD = false;
        this.draggingPotionStatus = false;
        this.draggingScoreboard = false;
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    protected void actionPerformed(GuiButton button) {
    	if(button.id == 0) {
        	mc.displayGuiScreen(Nightmare.instance.clickGUI);
    	}
    }
    
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
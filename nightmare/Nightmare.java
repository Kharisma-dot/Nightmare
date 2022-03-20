package nightmare;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import nightmare.clickgui.ClickGUI;
import nightmare.command.CommandManager;
import nightmare.config.ConfigManager;
import nightmare.event.EventManager;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventKey;
import nightmare.fonts.api.FontManager;
import nightmare.fonts.impl.SimpleFontManager;
import nightmare.module.ModuleManager;
import nightmare.settings.SettingsManager;

public class Nightmare {
	
	public static Nightmare instance = new Nightmare();
	
	@Getter
	private String name = "Nightmare", version = "1.2";
	
    public SettingsManager settingsManager;
    public EventManager eventManager;
    public ModuleManager moduleManager;
    public ClickGUI clickGUI;
    public ConfigManager config;
    
    public CommandManager commandManager = new CommandManager();
    public FontManager fontManager = SimpleFontManager.create();
    
	public void startClient() {
	    settingsManager = new SettingsManager();
	    eventManager = new EventManager();
	    moduleManager = new ModuleManager();
	    clickGUI = new ClickGUI();
	    config = new ConfigManager();
	    
        eventManager.register(this);
        
        Nightmare.instance.moduleManager.getModuleByName("Freecam").setToggled(false);
	}
	
	public void stopClient() {
		eventManager.unregister(this);
	}
	
    @EventTarget
    public void onKey(EventKey event) {
        moduleManager.getModules().stream().filter(module -> module.getKey() == event.getKey()).forEach(module -> module.toggle());
    }
    
    public void sendChatMessage(String message) {
    	
    	message = "[" + Nightmare.instance.name + "]" +  " " + message;
    	
    	Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    } 
}
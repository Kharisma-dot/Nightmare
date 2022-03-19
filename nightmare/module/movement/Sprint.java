package nightmare.module.movement;

import java.util.ArrayList;

import net.minecraft.client.settings.KeyBinding;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventPreMotionUpdate;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.ChatUtils;
import nightmare.utils.PlayerUtils;

public class Sprint extends Module{

	public Sprint() {
		super("Sprint", 0, Category.MOVEMENT);
		
        ArrayList<String> options = new ArrayList<>();
        options.add("Normal");
        options.add("Omni");
        
        Nightmare.instance.settingsManager.rSetting(new Setting("Mode", this, "Normal", options));
	}
	
    @EventTarget
    public void onUpdate(EventUpdate event) {
    	String mode = Nightmare.instance.settingsManager.getSettingByName(this, "Mode").getValString();
    	
    	this.setDisplayName(this.getName() + " " + ChatUtils.GRAY + mode);
    	
    	if(mode.equals("Normal")) {
    		KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
    	}
    }

    
    @Override
    public void onDisable() {
        super.onDisable();
    	String mode = Nightmare.instance.settingsManager.getSettingByName(this, "Mode").getValString();
    	
    	if(mode.equals("Normal")) {
    		KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
    	}
    }
    
    @EventTarget
    public void onMotion(EventPreMotionUpdate event) {
    	String mode = Nightmare.instance.settingsManager.getSettingByName(this, "Mode").getValString();
    	
    	if(mode.equals("Omni")) {
            mc.thePlayer.setSprinting(PlayerUtils.isMoving());
    	}
    }
}
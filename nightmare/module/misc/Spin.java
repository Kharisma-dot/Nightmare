package nightmare.module.misc;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.entity.EntityPlayerSP;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventTick;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class Spin extends Module{

    private float yaw;
    
    private Random random = new Random();
    
    private int randomval;
	public Spin() {
		super("Spin", 0, Category.MISC);

		ArrayList<String> options = new ArrayList<>();
		
		options.add("Left");
		options.add("Right");
		options.add("Random");
		
		Nightmare.instance.settingsManager.rSetting(new Setting("Speed", this, 55, 1, 100, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Mode", this, "Left", options));
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		String mode = Nightmare.instance.settingsManager.getSettingByName(this, "Mode").getValString();
		
		if(Nightmare.instance.moduleManager.getModuleByName("Perspective").isToggled()) {
			this.setToggled(false);
			return;
		}
		
		if(mode.equals("Left")) {
			this.spinLeft();
		}else if(mode.equals("Right")) {
			this.spinRight();
		}else if(mode.equals("Random")) {
			
			if(randomval == 0) {
				this.spinLeft();
			}
			
			if(randomval == 1) {
				this.spinRight();
			}
		}
	}
	
	@Override
	public void onEnable() {
		randomval = random.nextInt(2);
		if(mc.thePlayer != null & mc.theWorld != null) {
	        this.yaw = mc.thePlayer.rotationYaw;
		}
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		if(mc.thePlayer != null & mc.theWorld != null) {
	        this.yaw = 0.0F;
		}
		super.onDisable();
	}
	
	public void spinLeft() {
        double left = (double)this.yaw + 360 - (double)mc.thePlayer.rotationYaw;
        EntityPlayerSP player;
        if (left < Nightmare.instance.settingsManager.getSettingByName(this, "Speed").getValDouble()) {
            player = mc.thePlayer;
            player.rotationYaw = (float)((double)player.rotationYaw + left);
        	toggle();
        } else {
            player = mc.thePlayer;
            player.rotationYaw = (float) ((double)player.rotationYaw + Nightmare.instance.settingsManager.getSettingByName(this, "Speed").getValDouble());
            if ((double)mc.thePlayer.rotationYaw >= (double)this.yaw + 360) {
            	toggle();
            }
        }
	}
	
	public void spinRight() {
        double right = (double)this.yaw - 360 - (double)mc.thePlayer.rotationYaw;
        EntityPlayerSP player;
        if (right > -Nightmare.instance.settingsManager.getSettingByName(this, "Speed").getValDouble()) {
            player = mc.thePlayer;
            player.rotationYaw = (float)((double)player.rotationYaw + right);
        	toggle();
        } else {
            player = mc.thePlayer;
            player.rotationYaw = (float) ((double)player.rotationYaw - Nightmare.instance.settingsManager.getSettingByName(this, "Speed").getValDouble());
            if ((double)mc.thePlayer.rotationYaw <= (double)this.yaw - 360) {
            	toggle();
            }
        }
	}
}
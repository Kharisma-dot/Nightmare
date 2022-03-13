package nightmare.module.misc;

import net.minecraft.client.entity.EntityPlayerSP;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventTick;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class Spin extends Module{

    private float yaw;
    
	public Spin() {
		super("Spin", 0, Category.MISC);

		Nightmare.instance.settingsManager.rSetting(new Setting("Speed", this, 55, 1, 100, false));
	}
	
	@Override
	public void onEnable() {
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
	
	@EventTarget
	public void onTick(EventTick event) {
        double left = (double)this.yaw + 360 - (double)mc.thePlayer.rotationYaw;
        EntityPlayerSP player;
        if (left < Nightmare.instance.settingsManager.getSettingByName(this, "Speed").getValDouble()) {
            player = mc.thePlayer;
            player.rotationYaw = (float)((double)player.rotationYaw + left);
            this.setToggled(false);
        } else {
            player = mc.thePlayer;
            player.rotationYaw = (float) ((double)player.rotationYaw + Nightmare.instance.settingsManager.getSettingByName(this, "Speed").getValDouble());
            if ((double)mc.thePlayer.rotationYaw >= (double)this.yaw + 360) {
                this.setToggled(false);
            }
        }
	}
}
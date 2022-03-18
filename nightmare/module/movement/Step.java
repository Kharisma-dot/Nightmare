package nightmare.module.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventStep;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class Step extends Module{

    public boolean stepping = false;
    
	public Step() {
		super("Step", 0, Category.MOVEMENT);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("Smooth", this, false));
	}

	@EventTarget
	public void onStep(EventStep event) {
        if (event.isPre() && !mc.thePlayer.movementInput.jump && mc.thePlayer.isCollidedVertically) {
            event.setStepHeight(1.0D);
        }else if (!event.isPre() && event.getRealHeight() > 0.5D && event.getStepHeight() > 0.0D && !mc.thePlayer.movementInput.jump && mc.thePlayer.isCollidedVertically) {
            stepping = true;
            if (event.getRealHeight() >= 0.87D) {
                double realHeight = event.getRealHeight();
                double height1 = realHeight * 0.42D;
                double height2 = realHeight * 0.75D;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + height1, mc.thePlayer.posZ, mc.thePlayer.onGround));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + height2, mc.thePlayer.posZ, mc.thePlayer.onGround));

            }
            if (Nightmare.instance.settingsManager.getSettingByName(this, "Smooth").getValBoolean()) {
                mc.timer.timerSpeed = 0.55F;
                (new Thread(() -> {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException var1) {
                        ;
                    }
                    stepping = false;
                    mc.timer.timerSpeed = 1.0F;
                })).start();
            } else {
                stepping = false;
            }
        }
	}
}

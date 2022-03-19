package nightmare.utils;

import net.minecraft.client.Minecraft;
import nightmare.event.impl.EventMove;
import nightmare.module.ModuleManager;

public class PlayerUtils {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
    public static void setMoveSpeed(EventMove event, double speed) {
        double forward = mc.thePlayer.moveForward;
        double strafe = mc.thePlayer.moveStrafing;
        float yaw = mc.thePlayer.rotationYaw;

        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }
    
    public static boolean isMoving() {
        return mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f;
    }
}

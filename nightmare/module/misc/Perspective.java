package nightmare.module.misc;

import org.lwjgl.opengl.Display;

import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;

public class Perspective extends Module{
	
	private static float cameraYaw = 0F;
	private static float cameraPitch = 0F;
	private int previousPerspective = 0;
	
	public Perspective() {
		super("Perspective", 0, Category.MISC);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		mc.gameSettings.thirdPersonView = 1;
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		if(mc.thePlayer == null || mc.theWorld == null) {
			this.setToggled(false);
			return;
		}
		
		previousPerspective = mc.gameSettings.thirdPersonView;
		cameraYaw = mc.thePlayer.rotationYaw;
		cameraPitch = mc.thePlayer.rotationPitch;
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		mc.gameSettings.thirdPersonView = previousPerspective;
	}
	
	public static float getCameraYaw() {
		return cameraYaw;
	}
	
	public static float getCameraPitch() {
		return cameraPitch;
	}
	
	public static boolean overrideMouse()
	{
		if (mc.inGameHasFocus && Display.isActive())
		{
			if (Nightmare.instance.moduleManager.getModuleByName("Perspective").isDisabled())
			{
				return true;
			}
			
			mc.mouseHelper.mouseXYChange();
			float f1 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			float f2 = f1 * f1 * f1 * 8.0F;
			float f3 = (float) mc.mouseHelper.deltaX * f2;
			float f4 = (float) mc.mouseHelper.deltaY * f2;

			cameraYaw += f3 * 0.15F;
			cameraPitch += f4 * 0.15F;

			if (cameraPitch > 90) cameraPitch = 90;
			if (cameraPitch < -90) cameraPitch = -90;
		}

		return false;
	}
}

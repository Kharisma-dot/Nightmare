package nightmare.utils;

import java.awt.Color;

import nightmare.Nightmare;

public class ColorUtils {

	public static int getBackgroundColor() {
		return new Color(0, 0, 0,110).getRGB();
	}
	
	public static int getClientColor() {
		return new Color((int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("HUD"), "Red").getValDouble(), (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("HUD"), "Green").getValDouble(), (int) Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("HUD"), "Blue").getValDouble()).getRGB();
	}
}

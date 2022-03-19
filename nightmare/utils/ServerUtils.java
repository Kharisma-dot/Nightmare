package nightmare.utils;

import net.minecraft.client.Minecraft;

public class ServerUtils {

	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static boolean isOnServer() {
		if(mc.getCurrentServerData() != null) {
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isHypixel() {
		if(isOnServer() && mc.getCurrentServerData().serverIP.contains("hypixel")) {
			return true;
		}else {
			return false;
		}
	}
}

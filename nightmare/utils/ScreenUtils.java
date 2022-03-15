package nightmare.utils;

import net.minecraft.client.Minecraft;

public class ScreenUtils {
	
    public static int getWitdh() {
        return Minecraft.getMinecraft().displayWidth / getScaleFactor();
    }

    public static int getHeight() {
        return Minecraft.getMinecraft().displayHeight / getScaleFactor();
    }
    
    public static int getScaleFactor() {
        int scaleFactor = 1;
        final boolean isUnicode = Minecraft.getMinecraft().isUnicode();
        int guiScale = Minecraft.getMinecraft().gameSettings.guiScale;
        if (guiScale == 0) {
            guiScale = 1000;
        }
        
        while (scaleFactor < guiScale && Minecraft.getMinecraft().displayWidth / (scaleFactor + 1) >= 320 && Minecraft.getMinecraft().displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        if (isUnicode && scaleFactor % 2 != 0 && scaleFactor != 1) {
            --scaleFactor;
        }
        return scaleFactor;
    }
}

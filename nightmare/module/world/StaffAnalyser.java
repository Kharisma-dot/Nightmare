package nightmare.module.world;

import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.minecraft.network.play.server.S02PacketChat;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventPreMotionUpdate;
import nightmare.event.impl.EventReceivePacket;
import nightmare.event.impl.EventRespawn;
import nightmare.gui.notification.NotificationManager;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.HttpUtils;
import nightmare.utils.ServerUtils;
import nightmare.utils.TimerUtils;

public class StaffAnalyser extends Module{

	public static String key = null;
	
    private TimerUtils timer = new TimerUtils();
    private CheckThread thread;
    
	public StaffAnalyser() {
		super("StaffAnalyser", 0, Category.WORLD);
		
        thread = new CheckThread();
        thread.start();
        
		Nightmare.instance.settingsManager.rSetting(new Setting("Delay", this, 60, 10, 120, true));
	}

    @EventTarget
    public void onPreUpdate(EventPreMotionUpdate event) {
        if (ServerUtils.isHypixel() && timer.delay(3000) && key == null) {
            mc.thePlayer.sendChatMessage("/api new");
            timer.reset();
        }
    }
    
    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat chatPacket = (S02PacketChat) event.getPacket();
            String chatMessage = chatPacket.getChatComponent().getUnformattedText();
            if (chatMessage.matches("Your new API key is ........-....-....-....-............")) {
                event.setCancelled(true);
                key = chatMessage.replace("Your new API key is ", "");
            }
        }
    }
    
    @EventTarget
    public void onRespawn(EventRespawn event) {
        key = null;
    }
}

class CheckThread extends Thread {
    int lastBannedCount = 0;

    @Override
    public void run() {
        while (true) {
            try {
                if (StaffAnalyser.key == null) {
                    sleep(1000);
                    continue;
                }
                sleep((long) (Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("StaffAnalyser"), "Delay").getValDouble() * 1000L));
                String result = HttpUtils.performGetRequest(new URL("https://api.hypixel.net/watchdogStats?key=" + StaffAnalyser.key));

                Gson gson = new Gson();
                BanQuantityListJSON banQuantityListJSON = gson.fromJson(result, BanQuantityListJSON.class);
                int staffTotal = banQuantityListJSON.getStaffTotal();
                
                if (lastBannedCount == 0) {
                    lastBannedCount = staffTotal;
                } else {
                    int banned = staffTotal - lastBannedCount;
                    lastBannedCount = staffTotal;

                    if (banned > 1) {
                    	if(Nightmare.instance.moduleManager.getModuleByName("HUD").isToggled() && Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("HUD"), "Notification").getValBoolean()) {
                    		NotificationManager.show("StaffAnalyser", "Staff banned " + banned + " players in " + Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("StaffAnalyser"), "Delay").getValDouble() + "s.", 10000);
                    	}else {
                    		Nightmare.instance.sendChatMessage("\247cStaff banned " + banned + " players in " + Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("StaffAnalyser"), "Delay").getValDouble() + "s.");
                    	}
                    } else{
                    	if(Nightmare.instance.moduleManager.getModuleByName("HUD").isToggled() && Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("HUD"), "Notification").getValBoolean()) {
                            NotificationManager.show("StaffAnalyser", "Staff didn't ban any player in " + Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("StaffAnalyser"), "Delay").getValDouble() + "s.", 10000);
                    	}else {
                    		Nightmare.instance.sendChatMessage("\247aStaff didn't ban any player in " + Nightmare.instance.settingsManager.getSettingByName(Nightmare.instance.moduleManager.getModuleByName("StaffAnalyser"), "Delay").getValDouble() + "s.");
                    	}
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

@NoArgsConstructor
@Data
class BanQuantityListJSON {
    @SerializedName("success")
    boolean success;
    @SerializedName("watchdog_lastMinute")
    int watchdogLastMinute;
    @SerializedName("staff_rollingDaily")
    int staffRollingDaily;
    @SerializedName("watchdog_total")
    int watchdogTotal;
    @SerializedName("watchdog_rollingDaily")
    int watchdogRollingDaily;
    @SerializedName("staff_total")
    int staffTotal;
}

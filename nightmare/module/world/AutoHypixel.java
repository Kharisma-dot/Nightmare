package nightmare.module.world;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiNewChat;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.ChatUtils;
import nightmare.utils.TimerUtils;

public class AutoHypixel extends Module{

	private TimerUtils timer = new TimerUtils();
	
	public AutoHypixel() {
		super("AutoHypixel", 0, Category.WORLD);
		
        ArrayList<String> options = new ArrayList<>();
        options.add("Solo-Normal");
        options.add("Solo-Insane");
        
		Nightmare.instance.settingsManager.rSetting(new Setting("AutoGG", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("AutoPlay", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Mode", this, "Solo-Normal", options));
		Nightmare.instance.settingsManager.rSetting(new Setting("Delay", this, 3, 0, 5, false));
		
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
		int time = (int) Nightmare.instance.settingsManager.getSettingByName(this, "Delay").getValDouble();
		String mode = Nightmare.instance.settingsManager.getSettingByName(this, "Mode").getValString();
		this.setDisplayName("AutoHypixel" + ChatUtils.GRAY + " " + mode);
		if(GuiNewChat.autoplay == true) {
			if(timer.delay(1000 * time)) {
				if(mode.equalsIgnoreCase("Solo-Normal")) {
					mc.thePlayer.sendChatMessage("/play solo_normal");
				}else {
					mc.thePlayer.sendChatMessage("/play solo_insane");
				}

				timer.reset();
				GuiNewChat.autoplay = false;
			}
		}else {
			timer.reset();
		}
	}
}
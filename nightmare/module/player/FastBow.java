package nightmare.module.player;

import java.util.ArrayList;

import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventTick;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.ChatUtils;

public class FastBow extends Module{

	public FastBow() {
		super("FastBow", 0, Category.PLAYER);
		
        ArrayList<String> options = new ArrayList<>();
        options.add("Vanilla");
        options.add("NCP");
        
        Nightmare.instance.settingsManager.rSetting(new Setting("Mode", this, "Vanilla", options));
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		
		String mode = Nightmare.instance.settingsManager.getSettingByName(this, "Mode").getValString();
		
		this.setDisplayName("Fastbow " + ChatUtils.GRAY + mode);
        if (mc.thePlayer.getItemInUseDuration() >= 15 || mode.equals("Vanilla")) {
            if (mc.thePlayer.onGround && mc.thePlayer.getItemInUse().getItem() instanceof ItemBow) {
                for (int i = 0; i < (mode.equals("Vanilla") ? 20 : 8); ++i)
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                    mc.playerController.onStoppedUsingItem(mc.thePlayer);
            }
        }
	}

}
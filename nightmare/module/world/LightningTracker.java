package nightmare.module.world;

import net.minecraft.network.play.server.S29PacketSoundEffect;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventReceivePacket;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.utils.ChatUtils;

public class LightningTracker extends Module{

	public LightningTracker() {
		super("LightningTracker", 0, Category.WORLD);
	}
	
	@EventTarget
	public void onPacket(EventReceivePacket e) {
		 if (e.getPacket() instanceof S29PacketSoundEffect) {
             S29PacketSoundEffect packet = (S29PacketSoundEffect) e.getPacket();

             if (packet.getSoundName().equals("ambient.weather.thunder")) {
                 int x = (int) packet.getX(), y = (int) packet.getY(), z = (int) packet.getZ();
                 Nightmare.instance.sendChatMessage(ChatUtils.YELLOW + "Lightning detected " + ChatUtils.RESET + "X: " + x + " Y: " + y + " Z:" + z);
             }
         }
	}
}
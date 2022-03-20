package nightmare.module.misc;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventReceivePacket;
import nightmare.event.impl.EventRespawn;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.utils.TimerUtils;

public class AntiAtlas extends Module{

    private List<UUID> user = new CopyOnWriteArrayList();
    private TimerUtils timer = new TimerUtils();
    
	public AntiAtlas() {
		super("AntiAtlas", 0, Category.MISC);
	}

    @EventTarget
    public void onWorldLoad(EventRespawn event) {
        user.clear();
        timer.reset();
    }
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
        for (NetworkPlayerInfo playerInfo : mc.getNetHandler().getPlayerInfoMap()) {
            UUID uuid = playerInfo.getGameProfile().getId();
            String name = playerInfo.getGameProfile().getName();

            if (timer.delay(5000) && !user.contains(uuid) && !uuid.equals(mc.thePlayer.getUniqueID())) {
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C01PacketChatMessage("/report " + name + " killaura fly speed"));
                user.add(uuid);
                timer.reset();
            }
        }
    }
 
    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) event.getPacket();

            if (packet.getChatComponent().getFormattedText().startsWith("\u00A7cThere is no player named")) {
                event.setCancelled(true);
            }
        }
    }
}

package nightmare.module.player;

import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventReceivePacket;
import nightmare.event.impl.EventSendPacket;
import nightmare.module.Category;
import nightmare.module.Module;

public class ChatBypass extends Module{

	public ChatBypass() {
		super("ChatBypass", 0, Category.PLAYER);
	}
	
    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) event.getPacket();
            if (packet.getChatComponent().getUnformattedText().contains("\u061c")) {
                packet.chatComponent = new ChatComponentText(packet.getChatComponent().getUnformattedText().replace("\u061c", ""));
            }
        }
    }
    
    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C01PacketChatMessage) {
            final C01PacketChatMessage packetChatMessage = (C01PacketChatMessage)event.getPacket();
            if (packetChatMessage.getMessage().startsWith("/")) {
                return;
            }
            event.setCancelled(true);
            final StringBuilder msg = new StringBuilder();
            for (final char character : packetChatMessage.getMessage().toCharArray()) {
                msg.append(character + "\u061c");
            }
            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C01PacketChatMessage(msg.toString().replaceFirst("%", "")));
        }
    }
}

package nightmare.event.impl;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;
import nightmare.event.Event;

public class EventSendPacket extends Event{

	@Getter
	@Setter
    private Packet packet;

    public EventSendPacket(Packet packet) {
        this.packet = packet;
    }
}
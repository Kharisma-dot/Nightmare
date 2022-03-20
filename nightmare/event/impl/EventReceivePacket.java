package nightmare.event.impl;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;
import nightmare.event.Event;

public class EventReceivePacket extends Event{

	@Getter
	@Setter
    private Packet packet;

    public EventReceivePacket(Packet packet) {
        this.packet = packet;
    }
}
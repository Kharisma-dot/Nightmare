package nightmare.event.impl;

import lombok.Getter;
import nightmare.event.Event;

public class EventRender3D extends Event{
	
	@Getter
	private float partialTicks;
	
	public EventRender3D(float partialTicks) {
		this.partialTicks = partialTicks;
	}
}

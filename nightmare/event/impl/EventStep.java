package nightmare.event.impl;

import lombok.Getter;
import lombok.Setter;
import nightmare.event.Event;

public class EventStep extends Event{
	
	@Getter
	@Setter
	private double stepHeight;
	
	@Getter
	@Setter
	private double realHeight;
	
	private boolean pre;

	public EventStep(boolean state, double stepHeight, double realHeight) {
		this.pre = state;
		this.stepHeight = stepHeight;
		this.realHeight = realHeight;
	}

	public EventStep(boolean state, double stepHeight) {
		this.pre = state;
		this.stepHeight = stepHeight;
		this.realHeight = this.realHeight;
	}

	public boolean isPre() {
		return this.pre;
	}
}

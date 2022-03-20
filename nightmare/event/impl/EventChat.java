package nightmare.event.impl;

import lombok.Getter;
import lombok.Setter;
import nightmare.event.Event;

public class EventChat extends Event{
	
	@Getter
	@Setter
	public String message;
	
	public EventChat(String message) {
		this.message = message;
	}
}
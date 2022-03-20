package nightmare.event.impl;

import lombok.Getter;
import nightmare.event.Event;

public class EventKey extends Event {

	@Getter
    private int key;

    public EventKey(int key) {
        this.key = key;
    }
}
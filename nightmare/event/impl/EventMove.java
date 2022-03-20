package nightmare.event.impl;

import lombok.Getter;
import lombok.Setter;
import nightmare.event.Event;
import nightmare.utils.LocationUtils;

public class EventMove extends Event {

	@Getter
	@Setter
    public double x, y, z;
	
	@Getter
	@Setter
    private LocationUtils location;

    public EventMove(LocationUtils location, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.location = location;
    }
}


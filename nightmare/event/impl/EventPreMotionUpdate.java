package nightmare.event.impl;

import lombok.Getter;
import lombok.Setter;
import nightmare.event.Event;

public class EventPreMotionUpdate extends Event {

	@Getter
	@Setter
    public float yaw, pitch;
	
	@Getter
	@Setter
    private boolean ground;
    
    @Getter
    public double x, y, z;

    public EventPreMotionUpdate(float yaw, float pitch, boolean ground, double x, double y, double z) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
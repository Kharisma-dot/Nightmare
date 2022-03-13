package nightmare.event.impl;

import nightmare.event.Event;
import nightmare.utils.LocationUtils;

public class EventMove extends Event {

    public double x;
    public double y;
    public double z;
    private LocationUtils location;

    public EventMove(LocationUtils location, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.location = location;
    }

    public double getX() {
        return this.x;
    }

    public LocationUtils getLocation() {
        return this.location;
    }

    public void setLocation(LocationUtils location) {
        this.location = location;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
    
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
}


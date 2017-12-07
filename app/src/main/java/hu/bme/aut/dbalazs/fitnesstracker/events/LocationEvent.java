package hu.bme.aut.dbalazs.fitnesstracker.events;

import android.location.Location;

/** Event class for Location service  */

public class LocationEvent {
    Location location;

    public LocationEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

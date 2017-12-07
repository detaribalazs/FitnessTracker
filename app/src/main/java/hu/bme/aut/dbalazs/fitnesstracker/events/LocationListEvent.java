package hu.bme.aut.dbalazs.fitnesstracker.events;

import android.location.Location;

import java.util.ArrayList;


/**
 * Created by Balazs on 2017. 12. 07..
 */

public class LocationListEvent {
    private ArrayList<Location> locationList;

    public LocationListEvent(ArrayList<Location> locationList) {
        this.locationList = locationList;
    }

    public ArrayList<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(ArrayList<Location> locationList) {
        this.locationList = locationList;
    }
}

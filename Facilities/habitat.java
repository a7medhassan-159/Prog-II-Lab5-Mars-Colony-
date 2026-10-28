package Facilities;

import colony.Location;

public class habitat extends Facilities {
    private int crewcapacity;

    public habitat(String name, Location location, int capacity, boolean isOperational, int crewcapacity) {
        super(name, location, capacity, isOperational);
        this.crewcapacity = crewcapacity;
    }

    public int getCrewcapacity() {
        return crewcapacity;
    }
   
    
}

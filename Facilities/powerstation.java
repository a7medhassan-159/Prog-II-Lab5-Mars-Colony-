package Facilities;
import  colony.*;
import resources.*;

public class powerstation extends Facilities {

    public powerstation(String name, Location location, int capacity, boolean isOperational,
            ResourceType... resourceTypes) {
        super(name, location, capacity, isOperational,ResourceType.BATTERY_POWER);
    }

}

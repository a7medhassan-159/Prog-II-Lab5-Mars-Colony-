package Facilities;
import  colony.*;
import resources.*;

public class communicationstation extends Facilities {
    
    public communicationstation(String name, Location location, int capacity, boolean isOperational,
            ResourceType... resourceTypes) {
        super(name, location, capacity, isOperational,ResourceType.SPARE_PARTS);
    }

    public String getFacilityType() {
        return "Communication Station";
    }
    
    
    public boolean providesCommunication() {
        return true;
    }
}

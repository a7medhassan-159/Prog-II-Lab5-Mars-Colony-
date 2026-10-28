package Facilities;

import colony.Location;
import resources.*;

public class habitat extends Facilities {

    public habitat(String name, Location location, int capacity, boolean isOperational, ResourceType... resourceTypes) {
        super(name, location, capacity, isOperational,ResourceType.FOOD, ResourceType.WATER, ResourceType.OXYGEN);
        
    }

    

  
    
}

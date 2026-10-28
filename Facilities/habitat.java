package Facilities;

import colony.Location;
import  colony.*;
import java.util.*;
import agents.*;
import resources.*;

public class habitat extends Facilities {
    private final Set<ResourceType> resources = EnumSet.noneOf(ResourceType.class);

    public habitat(String name, Location location, int capacity, boolean isOperational, ResourceType... resourceTypes) {
        super(name, location, capacity, isOperational,ResourceType.FOOD, ResourceType.WATER, ResourceType.OXYGEN);
        
    }

    

  
    
}

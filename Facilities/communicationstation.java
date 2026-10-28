package Facilities;
import  colony.*;
import java.util.*;
import agents.*;
import resources.*;

public class communicationstation extends Facilities {
    
    public communicationstation(String name, Location location, int capacity, boolean isOperational,
            ResourceType... resourceTypes) {
        super(name, location, capacity, isOperational,ResourceType.SPARE_PARTS);
    }

}

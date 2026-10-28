package Facilities;
import  colony.*;
import resources.*;

public class Laboratory extends Facilities {

    public Laboratory(String name, Location location, int capacity, boolean isOperational,
            ResourceType... resourceTypes) {
        super(name, location, capacity, isOperational,ResourceType.MEDICAL_SUPPLIES);
    }
    
    

}

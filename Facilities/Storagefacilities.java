package Facilities;
import  colony.*;
import resources.*;

public class Storagefacilities extends Facilities   {

public Storagefacilities(String name, Location location, int capacity, boolean isOperational,
        ResourceType... resourceTypes) {
    super(name, location, capacity, isOperational,resourceTypes);
}



}

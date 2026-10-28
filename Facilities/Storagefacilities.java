package Facilities;
import  colony.*;
import java.util.*;
import agents.*;
import resources.*;

public class Storagefacilities extends Facilities   {
private final Set<ResourceType> resources = EnumSet.noneOf(ResourceType.class);

public Storagefacilities(String name, Location location, int capacity, boolean isOperational,
        ResourceType... resourceTypes) {
    super(name, location, capacity, isOperational,ResourceType.FUEL);
}



}

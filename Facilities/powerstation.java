package Facilities;
import  colony.*;
import java.util.*;
import agents.*;
import resources.*;

public class powerstation extends Facilities {
    private final Set<ResourceType> resources = EnumSet.noneOf(ResourceType.class);

    public powerstation(String name, Location location, int capacity, boolean isOperational,
            ResourceType... resourceTypes) {
        super(name, location, capacity, isOperational,ResourceType.BATTERY_POWER);
    }

}

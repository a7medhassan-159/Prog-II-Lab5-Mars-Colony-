package Facilities;

import  colony.*;
import java.util.*;
import agents.*;
import resources.*;

public abstract class Facilities {

    private final String name;
    private final Location location;
    private final Set<Capability> capabilities = EnumSet.noneOf(Capability.class);
    private int capacity;
    private final Map<ResourceType, Integer> stock = new EnumMap<>(ResourceType.class);
    private boolean isOperational;
    //private equipemnt
    public Facilities(String name, Location location, int capacity, boolean isOperational) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.isOperational = isOperational;
    }
    public String getName() {
        return name;
    }
    public Location getLocation() {
        return location;
    }
    public Set<Capability> getCapabilities() {
        return capabilities;
    }
    public int getCapacity() {
        return capacity;
    }
    public boolean isOperational() {
        return isOperational;
    }
    public Map<ResourceType, Integer> getStock() {
        return stock;
    }
    public void addresource(ResourceType type, int quantity) {
        stock.put(type, stock.getOrDefault(type, 0) + quantity);
    }
       






}

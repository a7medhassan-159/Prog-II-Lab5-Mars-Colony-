package Facilities;

import  colony.*;
import java.util.*;
import agents.*;
import resources.*;

public abstract class Facilities implements Location {

    private final String name;
    private final Location location ;
    private final Set<Capability> capabilities = EnumSet.noneOf(Capability.class);
    private int capacity;
    private final Map<ResourceType, Resources> stock = new EnumMap<>(ResourceType.class);
    private boolean isOperational;
    private boolean isSheltered;
    private final List<Equipment> equipment = new ArrayList<>();

    public Facilities(String name, Location location, int capacity, boolean isOperational,ResourceType... resourceTypes){
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.isOperational = isOperational;
        for (ResourceType type : resourceTypes) {
            addresource(type, 0);
        }
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
    public Map<ResourceType, Resources> getStock() {
        return stock;
    }
    public List<Equipment> getEquipment() {
        return equipment;
    }    
    public int getResourceQuantity(ResourceType type) {
        Resources existing = stock.get(type);
        return existing != null ? existing.getQuantity(type) : 0;
    }

    public Equipment findAvailableEquipment(EquipmentType type) {
        for (Equipment e : equipment) {
            if (e.getType() == type && e.isAvailableForUse()) {
                return e;
            }
        }
        return null;
    }

    public boolean providesCommunication() {return false;}
    
    public void addresource(ResourceType type, int quantity) {
        if (type == null || quantity <= 0) {
            System.out.println("Invalid resource type or quantity.");
            return;
        }
        Resources res = stock.get(type);

        if (res == null) {
            res = new Resources(); 
            stock.put(type, res); 
        }
        res.addQuantity(type, quantity);
    }

    public boolean consumeresource(ResourceType type, int quantity) {
        if (type == null || quantity <= 0) {
            return false;
        }
        Resources existing = stock.get(type);
        return existing != null && existing.consume(type, quantity);
    }

    public void addEquipment(Equipment equipment) {
        if (equipment != null) {
            this.equipment.add(equipment);
        }
    }

    public boolean isSheltered(){
        return isSheltered;
    }
}

    
       


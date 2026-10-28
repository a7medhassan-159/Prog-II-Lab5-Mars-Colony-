package Facilities;

import  colony.*;
import java.util.*;
import agents.*;
import resources.*;

public abstract class Facilities implements Location {

    private final String name;
    private final Location location;
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
            stock.put(type, new Resources(type, 0));
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
        return existing != null ? existing.getQuantity() : 0;
    }



    public void addresource(ResourceType type, int quantity) {
        if (type == null || quantity < 0) {
            System.out.println("Invalid resource type or quantity. Cannot add to stock.");
            return;

        }
           Resources existing = stock.get(type);
           if(existing != null) {
               existing.addQuantity(quantity);
           } else {
               stock.put(type, new Resources(type, quantity));
           }
         
        }
    public boolean consumeresource(ResourceType type, int quantity) 
    {
        Resources existing = stock.get(type);
        return existing != null && existing.consume(quantity);
    }
    public void addEquipment(Equipment equipment) {
        if (equipment != null) {
            this.equipment.add(equipment);
        }
    }
    public boolean hasoperationalequipment()
    {
      for (Equipment item : equipment) if (item.isOperational()) return true;
        return false;  
    }

    public boolean isSheltered(){
        return isSheltered;
    }
}

    
       


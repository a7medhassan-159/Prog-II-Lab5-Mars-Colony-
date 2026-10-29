package resources;

import Facilities.EquipmentType;

public class Equipment {
    private final String id;
    private final String name;
    private final EquipmentType type;
    private boolean damaged;
    private boolean inUse;

    public Equipment(String id, String name, EquipmentType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    
    public String getName() {
        return name;
    }

    public EquipmentType getType() {
        return type;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public boolean isFunctional() {
        return !damaged;
    }

    public boolean isAvailableForUse() {
        return !damaged && !inUse;
    }

    public void damage() {
        damaged = true;
    }

    
    public boolean isDamaged() {
        return damaged;
    }

    
    public void repair() {
        damaged = false;
    }

    
    public String toString() {
        return name + " (" + type + (damaged ? ", DAMAGED" : inUse ? ", in use" : "") + ")";
    }
}

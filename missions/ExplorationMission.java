package missions;

import agents.*;
import environment.*;
import Facilities.*;
import resources.*;
import colony.*;

import java.util.*;

public class ExplorationMission extends Mission {

    private final String region;
    private Equipment equipment;

    public ExplorationMission(String id, String region, String requiredSkill, int duration) {
        super(id, "Exploration of " + region, duration);
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    
    public List<Capability> getRequiredCapabilities() {
        return Arrays.asList(Capability.SURFACE_TRAVEL, Capability.SCIENTIFIC_RESEARCH);
    }

    
    public Resources getResourceRequirement() {
        Resources req = new Resources();
        req.addQuantity(ResourceType.FUEL, 10);
        return req;
    }


    public boolean isSurfaceMission() {
        return true;
    }


    
    public String checkEnvironment(Environment environment) {
        return environment.isDustStorm() ? "Surface operations are not allowed during a dust storm." : null;
    }

    
    public String checkPreconditions(MarsColony colony) {
        if (colony.findAvailableEquipment(EquipmentType.EXPLORATION) == null) {
            return "Required equipment (" + EquipmentType.EXPLORATION + ") is not available";
        }
        return null;
    }

    
    protected void onAssigned(MarsColony colony) {
        equipment = colony.findAvailableEquipment(EquipmentType.EXPLORATION);
        if (equipment != null) {
            equipment.setInUse(true);
        }
    }

    
    protected String checkOngoingExtras(MarsColony colony) {
        if (equipment != null && equipment.isDamaged()) {
            return "Equipment failure: " + equipment.getName();
        }
        return null;
    }

    
    protected void onReleased() {
        if (equipment != null) {
            equipment.setInUse(false);
        }
    }
}

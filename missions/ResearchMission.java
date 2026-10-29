package missions;

import agents.*;
import colony.*;
import Facilities.*;
import resources.*;

import java.util.*;

public class ResearchMission extends Mission {

    private final Laboratory laboratory;
    private Equipment equipment;

    public ResearchMission(String id, Laboratory laboratory, int duration) {
        super(id, "Research in " + laboratory.getName(), duration);
        this.laboratory = laboratory;
    }

    
    public List<Capability> getRequiredCapabilities() {
        return Collections.singletonList(Capability.SCIENTIFIC_RESEARCH);
    }

   
    public Resources getResourceRequirement() {
                Resources req = new Resources();
        req.addQuantity(ResourceType.WATER, 5);
        req.addQuantity(ResourceType.BATTERY_POWER, 10);
        return req;
    }



    
    public List<Facilities> getTargetFacilities() {
        return Collections.<Facilities>singletonList(laboratory);
    }

    
    public List<Facilities> getRequiredFacilities() {
        return Collections.<Facilities>singletonList(laboratory);
    }

    
    public String checkPreconditions(MarsColony colony) {
        if (!laboratory.isOperational()) {
            return laboratory.getName() + " is not operational";
        }
        if (laboratory.findAvailableEquipment(EquipmentType.SCIENTIFIC) == null) {
            return "No functional scientific equipment available in " + laboratory.getName();
        }
        return null;
    }

    
    protected void onAssigned(MarsColony colony) {
        equipment = laboratory.findAvailableEquipment(EquipmentType.SCIENTIFIC);
        if (equipment != null) {
            equipment.setInUse(true);
        }
    }

    
    protected void onStarted(MarsColony colony) {
        for (AbstractAgent a : getAssignedAgents()) {
            a.setLocation(laboratory);
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

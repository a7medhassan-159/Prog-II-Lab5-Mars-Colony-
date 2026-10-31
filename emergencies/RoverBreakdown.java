package emergencies;

import agents.*;
import colony.*;
import resources.*;
import java.util.*;

public class RoverBreakdown extends Emergency {

    private final ExplorationRover rover;

    public RoverBreakdown(String id, ExplorationRover rover) {
        super(id, "Breakdown of " + rover.getName());
        this.rover = rover;
    }

    
    public List<Capability> getRequiredResponders() {
        return Arrays.asList(Capability.ENGINEERING_REPAIR, Capability.ROBOTIC_REPAIR);
    }

    
    protected Resources getResponseResources() {
        Resources req = new Resources();
        req.addQuantity(ResourceType.SPARE_PARTS, 2);
        req.addQuantity(ResourceType.BATTERY_POWER, 5);
        return req;
    }

    public Location getLocation() {
        return rover.getLocation();
    }

    protected void onDetected(MarsColony colony) {
        rover.markDamaged();
        colony.log("  " + rover.getName() + " is DAMAGED at " + rover.getLocation().getName()
                + "; repair needs " + getResponseResources());
    }


    protected void onResolved(MarsColony colony) {
        rover.repair();
        colony.log("  " + rover.getName() + " repaired on site");
    }


    protected void onUnresolved(MarsColony colony) {
        colony.log("  " + rover.getName() + " remains damaged at " + rover.getLocation().getName()
                + " (create a RepairMission / RescueMission to recover it)");
    }
}

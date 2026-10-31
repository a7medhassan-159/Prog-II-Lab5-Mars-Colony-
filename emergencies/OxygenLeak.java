package emergencies;

import agents.*;
import colony.*;
import Facilities.*;
import resources.*;
import java.util.*;


public class OxygenLeak extends Emergency {

    private final habitat habitat;
    private final int oxygenLoss;
    private int totalOxygenLost;

    public OxygenLeak(String id, habitat habitat, int oxygenLoss) {
        super(id, "Oxygen leak in " + habitat.getName());
        this.habitat = habitat;
        this.oxygenLoss = oxygenLoss;
    }

    public int getTotalOxygenLost() {
        return totalOxygenLost;
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
        return habitat;
    }

    
    protected String checkPreconditions(MarsColony colony) {
        return habitat.isOperational() ? null : habitat.getName() + " is not operational";
    }


    private int removeOxygen(int amount) {
        int available = habitat.getResourceQuantity(ResourceType.OXYGEN);
        int lost = Math.min(available, amount);
        if (lost > 0) {
            habitat.consumeresource(ResourceType.OXYGEN, lost);
        }
        return lost;
    }

    protected void onDetected(MarsColony colony) {
        int lost = removeOxygen(oxygenLoss);
        totalOxygenLost += lost;
        habitat.startOxygenLeak();
        colony.log("  Leak detected in " + habitat.getName() + ": -" + lost + " oxygen (habitat now "
                + habitat.getResourceQuantity(ResourceType.OXYGEN) + ")");
    }

    protected void onResolved(MarsColony colony) {
        habitat.repairOxygenLeak();
        colony.log("  Leak repaired. Total oxygen lost: " + totalOxygenLost);
    }

    protected void onUnresolved(MarsColony colony) {
        int lost = removeOxygen(oxygenLoss);   
        totalOxygenLost += lost;
        colony.log("  Leak NOT repaired: additional -" + lost + " oxygen. Total oxygen lost: " + totalOxygenLost
                + " (habitat now " + habitat.getResourceQuantity(ResourceType.OXYGEN) + ")");
    }
}

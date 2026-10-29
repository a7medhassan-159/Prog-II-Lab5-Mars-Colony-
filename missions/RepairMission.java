package missions;

import agents.*;
import colony.*;
import Facilities.*;
import resources.*;

import java.util.*;


public class RepairMission extends Mission {

    private final Repairable target;

    public RepairMission(String id, Repairable target, int duration) {
        super(id, "Repair of " + target.getName(), duration);
        this.target = target;
    }

    public Repairable getTarget() {
        return target;
    }

    @Override
    public List<Capability> getRequiredCapabilities() {
        return Arrays.asList(Capability.ENGINEERING_REPAIR, Capability.ROBOTIC_REPAIR);
    }

    
    public Resources getResourceRequirement() {
        Resources req = new Resources();
        req.addQuantity(ResourceType.SPARE_PARTS, 2);
        req.addQuantity(ResourceType.BATTERY_POWER, 5);
        return req;
    }



    
    public List<Facilities> getTargetFacilities() {
        if (target instanceof Facilities) {
            return Collections.singletonList((Facilities) target);
        }
        return Collections.emptyList();
    }

    
    public String checkPreconditions(MarsColony colony) {
        if (!target.isDamaged()) {
            return target.getName() + " is not damaged, nothing to repair";
        }
        return null;
    }

    protected void onCompleted(MarsColony colony) {
        target.repair();
    }
}

package emergencies;

import agents.*;
import colony.*;
import Facilities.*;
import resources.*;
import java.util.*;


public class Fire extends Emergency {

    private static final int OXYGEN_BURNED = 15;
    private static final int INJURY = 40;
    private static final int TREATMENT = 30;

    private final Laboratory laboratory;
    private final List<CrewMember> affectedPersonnel = new ArrayList<>();

    public Fire(String id, Laboratory laboratory) {
        super(id, "Fire in " + laboratory.getName());
        this.laboratory = laboratory;
    }

    public List<AbstractAgent> getAffectedPersonnel() {
        return Collections.unmodifiableList(affectedPersonnel);
    }

    
    public List<Capability> getRequiredResponders() {
        return Collections.singletonList(Capability.FIRE_RESPONSE);
    }

    
    protected Resources getResponseResources() {
        Resources req = new Resources();
        req.addQuantity(ResourceType.SPARE_PARTS, 2);
        req.addQuantity(ResourceType.WATER, 10);
        return req;
    }

    
    public Location getLocation() {
        return laboratory;
    }

    
    protected void onDetected(MarsColony colony) {
        laboratory.setOperational(false);
        for (Equipment e : laboratory.getEquipment()) {
            e.damage();
        }

        for (CrewMember c : colony.getCrew()) {
            if (c.getLocation() == laboratory) {
                c.reduceHealth(INJURY);
                affectedPersonnel.add(c);
            }
        }

        Resources oxygen = new Resources();
        oxygen.addQuantity(ResourceType.OXYGEN, OXYGEN_BURNED);
        colony.getResourceManager().consume(oxygen);

        colony.log("Fire in " + laboratory.getName());
    }

    protected void onResolved(MarsColony colony) {
        laboratory.setOperational(true);

        for (CrewMember c : affectedPersonnel) {
            c.heal(TREATMENT);
        }
        colony.log("Fire extinguished, injured treated");
    }

    @Override
    protected void onUnresolved(MarsColony colony) {
        Resources oxygen = new Resources();
        oxygen.addQuantity(ResourceType.OXYGEN, OXYGEN_BURNED);
        colony.getResourceManager().consume(oxygen);

        colony.log("Fire still burning, more oxygen burned");
    }
}

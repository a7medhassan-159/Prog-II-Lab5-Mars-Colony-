package emergencies;

import agents.*;
import colony.*;
import resources.*;
import java.util.*;

/**
 * A crew member needs urgent medical attention.
 * Checks: appropriate medical officer available (the patient cannot treat himself), medical supplies exist,
 * the facility the patient is in is operational. After successful treatment the patient's health increases.
 */
public class MedicalEmergency extends Emergency {

    private static final int TREATMENT = 40;

    private final CrewMember patient;
    private final int healthLoss;

    public MedicalEmergency(String id, CrewMember patient, int healthLoss) {
        super(id, "Medical emergency: " + patient.getName());
        this.patient = patient;
        this.healthLoss = healthLoss;
    }

    
    public List<Capability> getRequiredResponders() {
        return Collections.singletonList(Capability.MEDICAL_CARE);
    }

    
    protected Resources getResponseResources() {
        Resources req = new Resources();
        req.addQuantity(ResourceType.MEDICAL_SUPPLIES, 2);
        req.addQuantity(ResourceType.OXYGEN, 5);
        return req;

    }

    
    public Location getLocation() {
        return patient.getLocation();
    }

    
    protected boolean isExcludedResponder(AbstractAgent agent) {
        return agent == patient;
    }

    @Override
    protected String checkPreconditions(MarsColony colony) {
        Location where = patient.getLocation();
        if (!where.isSheltered() || !where.isOperational()) {
            return "The facility treating " + patient.getName() + " is not operational (patient at "
                    + where.getName() + ")";
        }
        return null;
    }

    @Override
    protected void onDetected(MarsColony colony) {
        patient.reduceHealth(healthLoss);
        colony.log("  " + patient.getName() + " health dropped to " + patient.getHealth());
    }

    @Override
    protected void onResolved(MarsColony colony) {
        patient.heal(TREATMENT);
        colony.log("  " + patient.getName() + " treated, health now " + patient.getHealth());
    }

    @Override
    protected void onUnresolved(MarsColony colony) {
        colony.log("  " + patient.getName() + " remains untreated (health " + patient.getHealth() + ")");
    }
}

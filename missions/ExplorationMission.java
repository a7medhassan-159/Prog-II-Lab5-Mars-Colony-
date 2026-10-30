
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

    @Override
    public List<Capability> getRequiredCapabilities() {
        return Arrays.asList(
            Capability.SURFACE_TRAVEL,
            Capability.SCIENTIFIC_RESEARCH
        );
    }

    @Override
    public Resources getResourceRequirement() {
        Resources req = new Resources();
        req.addQuantity(ResourceType.FUEL, 10);
        return req;
    }

    @Override
    public boolean isSurfaceMission() {
        return true;
    }

    public String checkEnvironment(Environment environment) {
        if (environment.isDustStorm()) {
            return "Surface operations are not allowed during a dust storm.";
        }

        return null;
    }

    @Override
    public String checkPreconditions(MarsColony colony) {

        String environmentError = checkEnvironment(colony.getEnvironment());

        if (environmentError != null) {
            return environmentError;
        }

        if (colony.findAvailableEquipment(EquipmentType.EXPLORATION) == null) {
            return "Required equipment (" +
                   EquipmentType.EXPLORATION +
                   ") is not available";
        }

        return null;
    }

    @Override
    protected void onAssigned(MarsColony colony) {

        equipment = colony.findAvailableEquipment(EquipmentType.EXPLORATION);

        if (equipment != null) {
            equipment.setInUse(true);
        }
    }

    @Override
    protected String checkOngoingExtras(MarsColony colony) {

        // Check the equipment assigned to this mission.
        // Do not search for new equipment here.

        if (equipment == null) {
            return "Exploration equipment was not assigned.";
        }

        if (equipment.isDamaged()) {
            return "Equipment failure: " + equipment.getName();
        }

        return null;
    }

    @Override
    protected void onReleased() {

        if (equipment != null) {
            equipment.setInUse(false);
            equipment = null;
        }
    }
}

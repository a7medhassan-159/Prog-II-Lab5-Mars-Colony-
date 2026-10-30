
package emergencies;

import agents.*;
import colony.*;
import Facilities.habitat;
import resources.*;

public class EmergencyManager {

    public enum Status { DETECTED, IN_PROGRESS, RESOLVED, UNRESOLVED}

    private final MarsColony colony;

    private Status status = Status.DETECTED;
    private String lastReason = "";

    public EmergencyManager(MarsColony colony) {
        if (colony == null) {
            throw new IllegalArgumentException(
                    "Colony cannot be null");
        }

        this.colony = colony;
    }

    private CrewMember findHumanResponder() {

        for (CrewMember agent : colony.getCrew()) {

            if (agent.canHandleEmergency(
                    Capability.ENGINEERING_REPAIR)) {
                return agent;
            }
        }

        return null;
    }

    private Robot findRobotResponder() {

        for (Robot robot : colony.getRobots()) {

            if (robot.canHandleEmergency(
                    Capability.ROBOTIC_REPAIR)) {
                return robot;
            }
        }

        return null;
    }

    private void deductOxygen(habitat affectedHabitat,int amount) {

        int available = affectedHabitat.getResourceQuantity(ResourceType.OXYGEN);

        
        int actualLoss = Math.min(available, amount);  // avoid negative.

        if (actualLoss > 0) {
            affectedHabitat.consumeresource(ResourceType.OXYGEN,actualLoss);
        }

        colony.log("Oxygen lost: " + actualLoss);
    }


    private Status failEmergency(
            String id,
            String reason,
            habitat affectedHabitat,
            int oxygenLoss) {

        deductOxygen(affectedHabitat, oxygenLoss);   // An unresolved leak 

        status = Status.UNRESOLVED;
        lastReason = reason;

        colony.log("Emergency " + id + " UNRESOLVED: " + reason);

        return status;
    }

    public Status handleOxygenLeak(String id,habitat affectedHabitat,int oxygenLoss, int sparePartsRequired,boolean repairSucceeds) {

        if (id == null || id.trim().isEmpty()|| affectedHabitat == null|| oxygenLoss <= 0|| sparePartsRequired < 0) {

            throw new IllegalArgumentException(
                    "Invalid emergency parameters");
        }

        status = Status.DETECTED;
        lastReason = "";

        colony.log("OXYGEN LEAK DETECTED: " + id);

       
        affectedHabitat.startOxygenLeak();             // Mark active leak.

        
        deductOxygen(affectedHabitat, oxygenLoss);    //  Apply the initial oxygen loss.

        
        CrewMember engineer = findHumanResponder();   //  Find suitable responders.
        Robot robot = findRobotResponder();

        if (engineer == null || robot == null) {

            return failEmergency(id,"Required human or robotic responder unavailable",affectedHabitat,oxygenLoss);
        }

        
        Resources required = new Resources();       

        if (sparePartsRequired > 0) {
            required.addQuantity(
                    ResourceType.SPARE_PARTS,
                    sparePartsRequired);
        }

        if (!colony.getResourceManager().canSupply(required)) {

            return failEmergency(id,"Insufficient spare parts",affectedHabitat,oxygenLoss);
        }

        
        var engineerOriginalLocation = engineer.getLocation();
        var robotOriginalLocation = robot.getLocation();

       
        engineer.setLocation(affectedHabitat);
        robot.setLocation(affectedHabitat);

        engineer.setBusy(true);
        robot.setBusy(true);

        status = Status.IN_PROGRESS;

        colony.log("Emergency responders dispatched: "+ engineer.getName() + " and "+ robot.getName());

        try {

            if (!colony.getResourceManager().consume(required)) {

                return failEmergency(id,"Unable to consume repair resources",affectedHabitat,oxygenLoss);
            }

            
            if (repairSucceeds) {

                affectedHabitat.repairOxygenLeak();
                
                status = Status.RESOLVED;
                
                lastReason = "";

                colony.log("Emergency " + id + " RESOLVED");

            } else {

                return failEmergency(id,"Repair operation failed",affectedHabitat,oxygenLoss);
            }

        } finally {

            engineer.setBusy(false);
            robot.setBusy(false);

            engineer.setLocation(engineerOriginalLocation);
            robot.setLocation(robotOriginalLocation);

            colony.log("Emergency responders released");
        }

        return status;
    }

    public Status getStatus() {
        return status;
    }

    public String getLastReason() {
        return lastReason;
    }
}

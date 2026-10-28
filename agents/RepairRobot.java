package agents;

import colony.*;

/** Repairs damaged equipment and operates inside facilities. */
public class RepairRobot extends Robot {

    public RepairRobot(String id, String name, Location location, int battery) {
        super(id, name, location, battery, Capability.ROBOTIC_REPAIR, Capability.FIRE_RESPONSE);
    }

    public int getBatteryDrainPerUnit() {
        return 3;
    }
}

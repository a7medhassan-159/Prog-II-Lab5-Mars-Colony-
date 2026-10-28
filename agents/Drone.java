package agents;

import colony.*;
import environment.*;

public class Drone extends Robot {

    public Drone(String id, String name, Location location, int battery) {
        super(id, name, location, battery, Capability.AERIAL_RECON);
    }

    
    public int getBatteryDrainPerUnit() {
        return 4;
    }

    
    public String getEnvironmentRestriction(Environment environment) {
        return (environment.isCommunicationDisrupted())? "drones cannot be controlled during a communication disruption" : null;
    }
}

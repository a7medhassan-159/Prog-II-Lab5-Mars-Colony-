package agents;

import colony.*;
import environment.*;


public class ExplorationRover extends Robot {

    public ExplorationRover(String id, String name, Location location, int battery) {
        super(id, name, location, battery, Capability.SURFACE_TRAVEL);
    }

    public int getBatteryDrainPerUnit() {
        return 5;
    }

    
    public String getEnvironmentRestriction(Environment environment) {
        return environment.isDustStorm() ? "rovers cannot operate on the surface during a dust storm" : null;
    }
    
}

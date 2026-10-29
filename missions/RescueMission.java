package missions;

import agents.*;
import colony.*;
import environment.Environment;
import Facilities.*;
import resources.*;

import java.util.*;

public class RescueMission extends Mission {

    private final AbstractAgent Agent;
    private final Facilities destination;

    public RescueMission(String id, AbstractAgent stranded, Facilities destination, int duration) {
        super(id, "Rescue of " + stranded.getName(), duration);
        this.Agent = stranded;
        this.destination = destination;
    }

    
    public List<Capability> getRequiredCapabilities() {
        return Arrays.asList(Capability.SURFACE_TRAVEL, Capability.MEDICAL_CARE);
    }

    
    public Resources getResourceRequirement() {
        Resources req = new Resources();
        req.addQuantity(ResourceType.FUEL, 8);
        req.addQuantity(ResourceType.MEDICAL_SUPPLIES, 2);
        return req;

    }

    
    public boolean isSurfaceMission() {
        return true;
    }

   
    public boolean requiresCommunication() {
        return true;
    }

    
    public List<Facilities> getTargetFacilities() {
        return Collections.singletonList(destination);
    }

    
    public List<Facilities> getRequiredFacilities() {
        return Collections.singletonList(destination);
    }

    
    public String checkEnvironment(Environment environment) {
        return environment.isDustStorm() ? "Surface operations are not allowed during a dust storm." : null;
    }

    
    public String checkPreconditions(MarsColony colony) {
        if (Agent.getLocation().isSheltered()) {
            return Agent.getName() + "Not in danger !! " + Agent.getLocation().getName() + ")";
        }
        if (!destination.isOperational()) {
            return "Destination " + destination.getName() + " is not operational";
        }
        return null;
    }

    
    protected void onCompleted(MarsColony colony) {
        Agent.setLocation(destination);
    }
}

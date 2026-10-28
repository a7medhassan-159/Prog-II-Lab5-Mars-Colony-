package agents;
import  colony.*;
import environment.Environment;
import missions.Mission;

import java.util.*;


public abstract class AbstractAgent {

    private final String id;
    private final String name;
    private boolean busy;
    private Location location;
    private final Set<Capability> capabilities = EnumSet.noneOf(Capability.class);

    protected AbstractAgent(String id, String name, Location location, Capability... capabilities) {
        this.id = id;
        this.name = name;
        this.location = location;
        Collections.addAll(this.capabilities, capabilities);
    }

    
    public String getId() {return id;}

    
    public String getName() {return name;}


    public Location getLocation() {
        return location;
    }

    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public boolean isBusy() {return busy;}
    public void setBusy(boolean busy) {this.busy = busy;}

    public  boolean isAvailable() {
        return isOperational() && !isBusy();
    }
    

    
    public Set<Capability> getCapabilities() {return Collections.unmodifiableSet(capabilities);}

    public String getOperationalProblem(){
        return null;
    }

    public boolean isOperational() {
        return getOperationalProblem() == null;
    }

    public boolean hasCapability(Capability capability) {
        return getCapabilities().contains(capability);
    }

    public String toString() {
        return name + " (" + id + ")";
    }



    public boolean canPerform(Mission mission) {
        if (!isOperational()) {
            return false;
        }
        boolean matches = false;
        for (Capability c : mission.getRequiredCapabilities()) {
            if (hasCapability(c)) {
                matches = true;
                break;
            }
        }
        return matches ;
    }

    // ابقى بص  
    public String getEnvironmentRestriction(Environment environment) {
        return null;
    }

    
    public void applyHazard(int severity) {
        // default: unaffected
    }

    
    public boolean needsMedicalAttention() {
        return false;
    }

    
    public void receiveTreatment(int amount) {
        // default: nothing to treat
    }
}
   


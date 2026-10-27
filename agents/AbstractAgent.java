package agents;
import  colony.*;
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

    
    public Set<Capability> getCapabilities() {return Collections.unmodifiableSet(capabilities);}


    public String toString() {
        return name + " (" + id + ")";
    }
}
   


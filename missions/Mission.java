package missions;

import agents.*;
import environment.*;
import Facilities.*;
import resources.*;
import colony.*;
import java.util.*;


public abstract class Mission {

    private final String id;
    private final String name;
    private final int duration;
    private int timepassed; 
    private MissionStatus status = MissionStatus.CREATED;
    private String failureReason; 
    private final List<AbstractAgent> assignedAgents = new ArrayList<>();
    private final Map<AbstractAgent, Location> origins = new HashMap<>();
    private boolean released;


    protected Mission(String id, String name, int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Mission duration must be positive");
        }
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public int getTimepassed() {
        return timepassed;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public List<AbstractAgent> getAssignedAgents() {
        return Collections.unmodifiableList(assignedAgents);
    }
    
    public String toString() {
        return "[" + id + "] " + name + " (" + status
                + (status == MissionStatus.IN_PROGRESS ? " " + timepassed + "/" + duration : "")
                + (failureReason != null ? ", reason: " + failureReason : "") + ")";
    }
    public abstract List<Capability> getRequiredCapabilities();

    public abstract Resources getResourceRequirement();


    public boolean isSurfaceMission() {
        return false;
    }

    public boolean requiresCommunication() {
        return false;
    }
    public List<Facilities> getTargetFacilities() {
        return Collections.emptyList();
    }

    public List<Facilities> getRequiredFacilities() {
        return Collections.emptyList();
    }

    public String checkEnvironment(Environment environment) {
        return null;
    }



    protected void onReleased(){}; 
    

    //validation 

    
public final String validateTeam(List<AbstractAgent> agents) {
    return validateTeam(agents, true);
}

private String validateTeam(
        List<AbstractAgent> agents,
        boolean checkAvailability) {

    if (agents == null || agents.isEmpty()) {
        return "No agents were provided for the mission";
    }

    for (AbstractAgent a : agents) {

        if (a == null) {
            return "Invalid agent in mission team";
        }

        if (!a.canPerform(this)) {
            return "Agent " + a.getName()
                    + " does not have required capability";
        }
    }

    // Only check availability when assigning a new mission.
    if (checkAvailability) {
        for (AbstractAgent a : agents) {
            if (!a.isAvailable()) {
                return "Agent " + a.getName()
                        + " is not available";
            }
        }
    }

    if (!coversRequirements(new ArrayList<AbstractAgent>(agents))) {
        return "The team does not cover the required capabilities "
                + getRequiredCapabilities();
    }

    return null;
}


    private boolean coversRequirements(List<AbstractAgent> agents) {
        Set<Capability> teamCapabilities = new HashSet<>();
        
        for (AbstractAgent agent : agents) {
            teamCapabilities.addAll(agent.getCapabilities());
        }
        
        return teamCapabilities.containsAll(getRequiredCapabilities());
    }


    
    //ADDED


    

    public String checkPreconditions(MarsColony colony) { 
        return null;
    }

    
    protected String checkOngoingExtras(MarsColony colony) {
        return null;
    }

    protected void onAssigned(MarsColony colony) {
    }

    protected void onStarted(MarsColony colony) {
    }

    protected void onCompleted(MarsColony colony) {
    }

    public final String checkConditions(MarsColony colony, List<AbstractAgent> agents) {
        if (requiresCommunication() && !colony.isCommunicationAvailable()) {
            return "Communication capability is unavailable";
        }

        String teamError = validateTeam(agents);
        if (teamError != null) {
            return teamError;
        }

        String preError = checkPreconditions(colony);
        if (preError != null) {
            return preError;
        }

        return null; 
    }

    
public final String checkOngoing(MarsColony colony) {

    if (requiresCommunication()
            && !colony.isCommunicationAvailable()) {
        return "Communication is unavailable";
    }

    String teamError = validateTeam(assignedAgents, false);

    if (teamError != null) {
        return teamError;
    }

    for (Facilities facility : getRequiredFacilities()) {
        if (!facility.isOperational()) {
            return "Required facility "
                    + facility.getName()
                    + " is not operational";
        }
    }

    for (AbstractAgent agent : assignedAgents) {
        String problem = agent.getOperationalProblem();

        if (problem != null) {
            return "Agent " + agent.getName() + ": " + problem;
        }
    }

    return checkOngoingExtras(colony);
}

    private void requireStatus(MissionStatus expected, String action) {
        if (status != expected) {
            System.out.println("Cannot " + action + " mission " + id + " in state " + status + " (expected " + expected + ")");
        }
    }

    public final void assign(List<AbstractAgent> agents, MarsColony colony) {
        requireStatus(MissionStatus.CREATED, "assign");
        for (AbstractAgent a : agents) {
            assignedAgents.add(a);
            origins.put(a, a.getLocation());
            a.setBusy(true);
        }
        status = MissionStatus.ASSIGNED;
        onAssigned(colony);
    }

    
    public final void start(MarsColony colony) {
        requireStatus(MissionStatus.ASSIGNED, "start");
        status = MissionStatus.IN_PROGRESS;
        if (isSurfaceMission()) {
            for (AbstractAgent a : assignedAgents) {
                a.setLocation(SurfaceLocation.MARS_SURFACE);
            }
        }
        onStarted(colony);
    }


    public final void advance(MarsColony colony) {
        requireStatus(MissionStatus.IN_PROGRESS, "advance");
        timepassed++;
        if (timepassed >= duration) {
            status = MissionStatus.COMPLETED;
            onCompleted(colony);
        }
    }

    public final void fail(String reason) {
        if (status.isFinal()) {
            System.out.println("Mission " + id + " already finished (" + status + ")");
        }
        status = MissionStatus.FAILED;
        failureReason = reason;
    }

    public final void release() {
        if (released) {
            return;
        }
        released = true;
        for (AbstractAgent a : assignedAgents) {
            a.setBusy(false);
            Location origin = origins.get(a);
            if (a.isOperational() && origin != null) {
                a.setLocation(origin);
            }
        }
        onReleased();
    }




}

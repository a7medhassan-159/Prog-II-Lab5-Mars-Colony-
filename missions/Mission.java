package missions;

import agents.*;
import environment.*;
import Facilities.*;
import resources.*;

import java.util.*;


public abstract class Mission {

    private final String id;
    private final String name;
    private final int duration;
    private int timepassed; 
    private MissionStatus status = MissionStatus.CREATED;
    private String failureReason; 
    private final List<AbstractAgent> assignedAgents = new ArrayList<>();



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

    @Override
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



    protected void onReleased() {
    }

    //validation 

    public final String validateTeam(List<AbstractAgent> agents) {
        if (agents == null || agents.isEmpty()) {
            return "No agents were provided for the mission";
        }
        for (AbstractAgent a : agents) {
            if (!a.canPerform(this)) {
                return "Agent " + a.getName() + " does not have required capability";
            }
        }
        for (AbstractAgent a : agents) {
            if (!a.isAvailable()) {
                return "Agent " + a.getName() + " is not available ";
            }
        }
        if (!coversRequirements(new ArrayList<AbstractAgent>(agents))) {
            return "The team does not cover the required capabilities " + getRequiredCapabilities();
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



}

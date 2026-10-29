package colony;

import agents.*;
import environment.*;
import Facilities.*;
import missions.*;
import resources.*;

import java.util.*;


public class MarsColony {

    private final String name;
    private final List<CrewMember> crew = new ArrayList<>();
    private final List<Robot> robots = new ArrayList<>();
    private final List<Facilities> facilities = new ArrayList<>();

    private final Resources resourceManager = new Resources(facilities);
    private final Environment environment = new Environment();
    private final Reacordingevents Events = new Reacordingevents();
    private final Map<Integer, List<Runnable>> scheduled = new TreeMap<>();

    private Mission activeMission;
    private final List<Mission> missionHistory = new ArrayList<>();
    private Resources periodicConsumption = new Resources();
    private int currentTime;
    
    public MarsColony(String name) {
        this.name = name;
    }

    // setters and getters

    public String getName() {
        return this.name;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public List<CrewMember> getCrew() {
        return crew;
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public List<Facilities> getFacilities() {
        return facilities;
    }

    public Resources getResourceManager() {
        return resourceManager;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Reacordingevents getEventLog() {
        return Events;
    }

    public Mission getActiveMission() {
        return activeMission;
    }

    public List<Mission> getMissionHistory() {
        return missionHistory;
    }



    public void addCrewMember(CrewMember member) {
        crew.add(member);
    }

    public void addRobot(Robot robot) {
        robots.add(robot);
    }

    public void addFacility(Facilities facility) {
        facilities.add(facility);
    }

    public List<AbstractAgent> getAllAgents() {
        List<AbstractAgent> all = new ArrayList<>(crew);
        all.addAll(robots);
        return all;
    }

    public void setPeriodicConsumption(Resources requirement) {
        this.periodicConsumption = requirement;
    }



    // time and simu

    void setCurrentTime(int time) {
        this.currentTime = time;
    }

    public void processTimeUnit() {
        log("--- time unit ---");
        for (AbstractAgent a : getAllAgents()) {
            a.advanceTime();
        }
        if (!periodicConsumption.isEmpty() && !resourceManager.consume(periodicConsumption)) {
            log("  Warning: not enough resources for the periodic consumption " + periodicConsumption);
        }
        progressActiveMission();
    }

    private void progressActiveMission() {
        if (activeMission == null) {
            return;
        }
        Mission m = activeMission;
        String problem = m.checkOngoing(this);
        if (problem != null) {
            failActiveMission(problem);
            return;
        }
        if (m.getStatus() == MissionStatus.ASSIGNED) {
            m.start(this);
            log("  Mission " + m.getId() + " IN_PROGRESS");
        }
        m.advance(this);
        if (m.getStatus() == MissionStatus.COMPLETED) {
            log("  Mission " + m.getId() + " COMPLETED");
            closeActiveMission();
        } else {
            log("  Mission " + m.getId() + " progress " + m.getTimepassed() + "/" + m.getDuration());
        }
    }

    
    public void schedule(int atTime, Runnable action) {
        if (atTime <= currentTime) throw new IllegalArgumentException("Cannot schedule in the past (now T=" + currentTime + ")");
        scheduled.computeIfAbsent(atTime, k -> new ArrayList<>()).add(action);
    }

    public void advanceTime() { advanceTime(1); }

    public void advanceTime(int units) {
        for (int i = 0; i < units; i++) {
            currentTime++;
            List<Runnable> actions = scheduled.remove(currentTime);
            if (actions != null) for (Runnable a : actions) a.run();
            processTimeUnit();
        }
    }



    //missions 
    
    public void log(String message) {
        Events.record(currentTime, message);
    }

    public OperationResult submitMission(Mission mission, List<AbstractAgent> agents) {
        if (mission.getStatus() != MissionStatus.CREATED) {
            return OperationResult.failure("Mission " + mission.getId() + " was already processed ("
                    + mission.getStatus());
        }
        log("Mission submitted: " + mission.getId() + " - " + mission.getName());

        String reason = validateSubmission(mission, agents);
        if (reason == null && !resourceManager.consume(mission.getResourceRequirement())) {
            reason = "Insufficient resources"; // defensive: checked above
        }
        if (reason != null) {
            mission.fail(reason);
            missionHistory.add(mission);
            log("  Mission cannot be assigned. Reason: " + reason);
            return OperationResult.failure("Mission cannot be assigned. Reason: " + reason);
        }

        mission.assign(agents, this);
        activeMission = mission;
        log("  Mission ASSIGNED (team: " + agents + "; resources consumed: "
                + mission.getResourceRequirement() + ")");
        return OperationResult.success("Mission " + mission.getId() + " assigned");
    }

    private String validateSubmission(Mission mission, List<AbstractAgent> agents) {
        if (activeMission != null) {
            return "Another mission (" + activeMission.getId() + ") is already active";
        }
        String reason = mission.validateTeam(agents);
        if (reason != null) {
            return reason;
        }
        reason = mission.checkConditions(this, agents);
        if (reason != null) {
            return reason;
        }
        for (Facilities f : mission.getRequiredFacilities()) {
            if (!f.isOperational()) {
                return "Required facility " + f.getName() + " is not operational";
            }
        }
        reason = mission.checkPreconditions(this);
        if (reason != null) {
            return reason;
        }
        String shortage = resourceManager.findShortage(mission.getResourceRequirement());
        if (shortage != null) {
            return "Insufficient resources: " + shortage;
        }
        return null;
    }

    public void reevaluateActiveMission() {
        if (activeMission == null || activeMission.getStatus().isFinal()) {
            return;
        }
        String problem = activeMission.checkOngoing(this);
        if (problem != null) {
            failActiveMission(problem);
        }
    }

    private void failActiveMission(String reason) {
        Mission m = activeMission;
        m.fail(reason);
        log("  Mission " + m.getId() + " FAILED. Reason: " + reason);
        closeActiveMission();
    }

    private void closeActiveMission() {
        activeMission.release();
        missionHistory.add(activeMission);
        activeMission = null;
    }

    // environment 

    public void activateCondition(EnvironmentCondition condition) {
        environment.activate(condition);
        log("ENVIRONMENT: " + condition.getLabel() + " begins");
        reevaluateActiveMission();
    }

    public void deactivateCondition(EnvironmentCondition condition) {
        environment.deactivate(condition);
        log("ENVIRONMENT: " + condition.getLabel() + " ends");
    }

    public boolean isCommunicationAvailable() {
        if (environment.isCommunicationDisrupted()) {
            return false;
        }
        for (Facilities f : facilities) {
            if (f.providesCommunication() && f.isOperational()) {
                return true;
            }
        }
        return false;
    }


    public Equipment findAvailableEquipment(EquipmentType type) {
        for (Facilities f : facilities) {
            if (f.isOperational()) {
                Equipment e = f.findAvailableEquipment(type);
                if (e != null) {
                    return e;
                }
            }
        }
        return null;
    }



    public String generateStatusReport() {
        return "=== COLONY STATUS: " + getName() + " (T=" + getCurrentTime() + ") ===\n" +
            "Environment    : " + getEnvironment().toString() + "\n" +
            "Resources      : " + getResourceManager().getTotals() + "\n" +
            "Facilities     : " + getFacilities().size() + "\n" +
            "Agents         : " + getCrew().size() + " Crew, " + getRobots().size() + " Robots\n" +
            "Active Mission : " + (getActiveMission() == null ? "None" : getActiveMission()) + "\n";
    }


}

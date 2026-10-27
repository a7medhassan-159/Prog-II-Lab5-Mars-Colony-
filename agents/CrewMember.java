package agents;

import  colony.*;
import java.util.*;

public abstract class CrewMember extends AbstractAgent {

    //constants
    public static final int MIN_OPERATIONAL_HEALTH = 30;
    public static final int MAX_FATIGUE = 100;
    private static final int FATIGUE_GAIN_BUSY = 10;
    private static final int FATIGUE_RECOVERY = 5;


    private int health = 100;
    private int fatigue = 0;
    private final Set<String> skills = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    protected CrewMember(String id, String name, Location location,
                         Collection<String> skills, Capability... capabilities) {
        super(id, name, location, capabilities);
        if (skills != null) {
            this.skills.addAll(skills);
        }
    }

    public int getHealth() {return health;}

    public HealthStatus getHealthStatus() {return HealthStatus.fromHealth(health);}

    public void reduceHealth(int amount) {health = Math.max(1, health - Math.max(0, amount));}

    public void heal(int amount) {health = Math.min(100, health + Math.max(0, amount));}

    public int getFatigue() {return fatigue;}

    public Set<String> getSkills() {return Collections.unmodifiableSet(skills);}

    public boolean hasSkill(String skill) {return skill != null && skills.contains(skill);}
 
    public void advanceTime() {
        if (isBusy()) {
            fatigue = Math.min(MAX_FATIGUE, fatigue + FATIGUE_GAIN_BUSY);
        } else {
            fatigue = Math.max(0, fatigue - FATIGUE_RECOVERY);
        }
    }

}

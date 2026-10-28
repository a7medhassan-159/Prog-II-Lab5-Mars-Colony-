package agents;

import  colony.*;

public abstract class CrewMember extends AbstractAgent {

    //constants
    public static final int MIN_OPERATIONAL_HEALTH = 30;
    public static final int MAX_FATIGUE = 100;
    private static final int FATIGUE_GAIN_BUSY = 10;
    private static final int FATIGUE_RECOVERY = 5;


    private int health = 100;
    private int fatigue = 0;

    protected CrewMember(String id, String name, Location location,
                          Capability... capabilities) {
        super(id, name, location, capabilities);

    }

    public void performDuty() { 
        System.out.println("Performing a duty."); 
    }

    public int getHealth() {return health;}

    public HealthStatus getHealthStatus() {return HealthStatus.fromHealth(health);}


    public void reduceHealth(int amount) {
    if (amount > 0) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }
    }

    public void heal(int amount) {
        if (amount > 0) {
            health += amount;
            if (health > 100) {
                health = 100;
            }
        }
    }

    public String getOperationalProblem() {
        if (health < MIN_OPERATIONAL_HEALTH) {
            return "health too low (" + health + ")";
        }
        if (fatigue >= MAX_FATIGUE) {
            return "exhausted (fatigue " + fatigue + ")";
        }
        return null;
    }

    public boolean needsMedicalAttention() {
        return getHealthStatus() != HealthStatus.HEALTHY;
    }

    public void receiveTreatment(int amount) {
        heal(amount);
    }


    public int getFatigue() {return fatigue;}
 
    public void advanceTime() {
        if (isBusy()) {
            fatigue = Math.min(MAX_FATIGUE, fatigue + FATIGUE_GAIN_BUSY);
        } else {
            fatigue = Math.max(0, fatigue - FATIGUE_RECOVERY);
        }
    }
    
    public String toString() {
        String status;
        
        if (isOperational()) status = "Available";
        else  status = "unAvailable";

        return getName() + " [ID: " + getId() + "] | Health: " + health + " | Fatigue: " + fatigue + 
            " | Capabilities: " + getCapabilities() + " | Location: " + getLocation().getName() + " | Status: " + status;
    }

}

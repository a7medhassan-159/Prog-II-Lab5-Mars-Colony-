package agents;


import colony.*;
import missions.*;


public abstract class Robot extends AbstractAgent implements Repairable {

    private int battery;
    private DamageStatus damageStatus = DamageStatus.OPERATIONAL;

    protected Robot(String id, String name, Location location, int battery, Capability... capabilities) {
        super(id, name, location, capabilities);
        if (battery > 100) {
            this.battery = 100;
        } else if (battery < 0) {
            this.battery = 0;
        } else {
            this.battery = battery;
        };
    }

    public abstract int getBatteryDrainPerUnit();
    public abstract int getBatteryrechargePerUnit();

    public int getBatteryLevel() {
        return battery;
    }

    public void drainBattery(int amount) {
        if (amount > 0) {
            battery -= amount; 
            
        if (battery < 0) {
                battery = 0;
            }
        }
    }

    public void recharge(int amount) {
        if (amount > 0) {
            battery += amount; 
            
        if (battery > 100) {
                battery = 100;
            }
        }    
    }

    public DamageStatus getDamageStatus() {
        return damageStatus;
    }

    public void markDamaged() {
        damageStatus = DamageStatus.DAMAGED;
    }

    
    public boolean isDamaged() {
        return damageStatus == DamageStatus.DAMAGED;
    }

    
    public void repair() {
        damageStatus = DamageStatus.OPERATIONAL;
    }

    
    public String getOperationalProblem() {
        if (damageStatus == DamageStatus.DAMAGED) {
            return "equipment failure (robot damaged)";
        }
        if (battery <= 0) {
            return "insufficient battery (depleted)";
        }
        return null;
    }

    
    protected boolean meetsMissionConstraints(Mission mission) {
        return battery >= mission.getDuration() * getBatteryDrainPerUnit();
    }

    
    public void advanceTime() {
        if (isBusy()) {
            drainBattery(getBatteryDrainPerUnit());
        }
    }

    

    public String toString() {
        return getName() + " [ID: " + getId() + "] - Battery: " + battery + "% - Location: " + getLocation().getName() + " - Busy: " + isBusy();
    }
}

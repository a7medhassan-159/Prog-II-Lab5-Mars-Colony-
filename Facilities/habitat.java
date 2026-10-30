
package Facilities;

import colony.Location;
import resources.*;

public class habitat extends Facilities {

    private boolean oxygenLeakActive = false;

    public habitat(String name,Location location,int capacity,boolean isOperational,ResourceType... resourceTypes) {

             super(name,location,capacity,isOperational,ResourceType.FOOD,ResourceType.WATER,ResourceType.OXYGEN);

    }

    public void startOxygenLeak() {
        oxygenLeakActive = true;
    }

    public void repairOxygenLeak() {
        oxygenLeakActive = false;
    }

    public boolean hasOxygenLeak() {
        return oxygenLeakActive;
    }
}

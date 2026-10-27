package environment;

import java.util.*;


public class Environment {

    private final Set<EnvironmentCondition> active = EnumSet.noneOf(EnvironmentCondition.class);

    public void activate(EnvironmentCondition condition) {
        if (condition == EnvironmentCondition.NORMAL) {
            active.clear();
        } else {
            active.add(condition);
        }
    }

    public void deactivate(EnvironmentCondition condition) {
        active.remove(condition);
    }

    public boolean isActive(EnvironmentCondition condition) {
        if (condition == EnvironmentCondition.NORMAL) {
            return active.isEmpty();
        }
        return active.contains(condition);
    }

    public boolean isDustStorm() {
        return active.contains(EnvironmentCondition.DUST_STORM);
    }

    public boolean isCommunicationDisrupted() {
        return active.contains(EnvironmentCondition.COMMUNICATION_DISRUPTION);
    }

    public boolean isNormal() {
        return active.isEmpty();
    }

    public String describe() {
        if (active.isEmpty()) {
            return EnvironmentCondition.NORMAL.getLabel();
        }
        StringBuilder sb = new StringBuilder();
        for (EnvironmentCondition c : active) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(c.getLabel());
        }
        return sb.toString();
    }
}

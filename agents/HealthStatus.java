package agents;

public enum HealthStatus {
    HEALTHY,
    INJURED,
    CRITICAL;

    public static HealthStatus fromHealth(int health) {
        if (health >= 70) {
            return HEALTHY;
        }
        return (health >= CrewMember.MIN_OPERATIONAL_HEALTH) ? INJURED : CRITICAL;
    }
}

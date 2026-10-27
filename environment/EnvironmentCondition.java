package environment;

/** Environmental conditions that can affect colony operations. */
public enum EnvironmentCondition {
    NORMAL("Normal conditions"),
    DUST_STORM("Dust storm"),
    COMMUNICATION_DISRUPTION("Communication disruption");

    private final String label;

    EnvironmentCondition(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

package missions;


public enum MissionStatus {
    CREATED,
    ASSIGNED,
    IN_PROGRESS,
    COMPLETED,
    FAILED;

    public boolean isFinal() {
        return this == COMPLETED || this == FAILED;
    }
}

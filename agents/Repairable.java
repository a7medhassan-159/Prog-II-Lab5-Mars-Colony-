package agents;

/**
 * Something that can be damaged and later repaired (facility, equipment, robot).
 */
public interface Repairable {

    String getName();

    boolean isDamaged();

    void repair();
}

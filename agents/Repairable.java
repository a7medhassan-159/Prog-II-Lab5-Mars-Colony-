package agents;


public interface Repairable {

    String getName();

    boolean isDamaged();

    void repair();
}

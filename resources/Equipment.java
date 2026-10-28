package resources;

public class Equipment {
    private String name;
    private boolean operational;
    public Equipment(String name, boolean operational) {
        this.name = name;
        this.operational = operational;
    }
    public String getName() {
        return name;
    }
    public boolean isOperational() {
        return operational;
    }
    public void repair() {
        if (!operational) {
        operational = true;
    }
    }
}

package agents;


import  colony.*;

public class Engineer extends CrewMember {

    public Engineer(String id, String name, Location location, String... skills) {
        super(id, name, location,
                Capability.ENGINEERING_REPAIR, Capability.FIRE_RESPONSE);
    }
    
    @Override
    public void performDuty() { 
        System.out.println("Repairing equipment."); 
    }
}

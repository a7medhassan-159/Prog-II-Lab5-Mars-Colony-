package agents;

import  colony.*;
public class Scientist extends CrewMember {

    public Scientist(String id, String name, Location location) {
        super(id, name, location, Capability.SCIENTIFIC_RESEARCH);
    }

    @Override 

    public void performDuty() { 
        System.out.println("Conducting research."); 
    } 

}
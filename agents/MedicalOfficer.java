package agents;


import colony.*;

public class MedicalOfficer extends CrewMember {
    
    public MedicalOfficer(String id, String name, Location location, String... skills) {
        super(id, name, location,Capability.MEDICAL_CARE, Capability.FIRE_RESPONSE);
    }
    public void performDuty(){
        System.out.println("Healing a member");
    }
}

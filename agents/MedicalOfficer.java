package agents;

import java.util.*;


import colony.*;

public class MedicalOfficer extends CrewMember {
    
    public MedicalOfficer(String id, String name, Location location, String... skills) {
        super(id, name, location, Arrays.asList(skills),
                Capability.MEDICAL_CARE, Capability.FIRE_RESPONSE);
    }
}

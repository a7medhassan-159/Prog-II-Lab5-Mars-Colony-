package agents;


import java.util.*;
import  colony.*;

public class Engineer extends CrewMember {

    public Engineer(String id, String name, Location location, String... skills) {
        super(id, name, location, Arrays.asList(skills),
                Capability.ENGINEERING_REPAIR, Capability.FIRE_RESPONSE);
    }
}

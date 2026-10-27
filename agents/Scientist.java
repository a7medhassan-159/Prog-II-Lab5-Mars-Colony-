package agents;

import java.util.*;
import  colony.*;
public class Scientist extends CrewMember {

    public Scientist(String id, String name, Location location, String... skills) {
        super(id, name, location, Arrays.asList(skills), Capability.SCIENTIFIC_RESEARCH);
    }

}
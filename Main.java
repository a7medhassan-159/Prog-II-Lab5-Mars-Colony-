import colony.*;
import missions.*;
import agents.*;
import Facilities.*;
import resources.*;
import java.util.*;
import emergencies.*;

public class Main {

    static class World {
        MarsColony colony;
        Laboratory lab;
        powerstation power;
        Storagefacilities storage;
        habitat habitat;
        Scientist scientist;
        Engineer engineer;
        ExplorationRover rover;
        RepairRobot repairRobot;
    }

    static World buildWorld(int fuel, int oxygen) {
        World w = new World();
        w.colony = new MarsColony("Ares Base");

        w.lab = new Laboratory("Central Lab", new SurfaceLocation(), 10, true);
        w.power = new powerstation("Solar Power Station", new SurfaceLocation(), 20, true);
        w.storage = new Storagefacilities("Main Storage", new SurfaceLocation(), 50, true,
                ResourceType.FUEL, ResourceType.SPARE_PARTS);
        w.habitat = new habitat("Main Habitat", new SurfaceLocation(), 10, true);

        w.lab.addEquipment(new Equipment("EQ-01", "Scientific Analyzer", EquipmentType.SCIENTIFIC));
        w.lab.addEquipment(new Equipment("EQ-02", "Exploration Equipment", EquipmentType.EXPLORATION));

        w.lab.addresource(ResourceType.WATER, 50);
        w.habitat.addresource(ResourceType.OXYGEN, oxygen);
        w.power.addresource(ResourceType.BATTERY_POWER, 50);
        if (fuel > 0) {
            w.storage.addresource(ResourceType.FUEL, fuel);
        }
        w.storage.addresource(ResourceType.SPARE_PARTS, 10);

        w.colony.addFacility(w.lab);
        w.colony.addFacility(w.power);
        w.colony.addFacility(w.storage);
        w.colony.addFacility(w.habitat);

        w.scientist = new Scientist("C-01", "Dr. Sarah", w.lab);
        w.engineer = new Engineer("C-02", "Eng. Alex", w.lab);
        w.rover = new ExplorationRover("R-01", "Rover Alpha", w.lab, 100);
        w.repairRobot = new RepairRobot("R-02", "Repair Robot", w.lab, 100);

        w.colony.addCrewMember(w.scientist);
        w.colony.addCrewMember(w.engineer);
        w.colony.addRobot(w.rover);
        w.colony.addRobot(w.repairRobot);

        return w;
    }

    static void printState(String label, World w) {
        System.out.println("\n  [" + label + "]");
        System.out.println("   Active mission : "
                + (w.colony.getActiveMission() == null ? "none"
                : w.colony.getActiveMission().getId()));
        System.out.println("   Resources      : " + w.colony.getResourceManager().getTotals());
        System.out.println("   Scientist busy : " + w.scientist.isBusy());
        System.out.println("   Engineer busy  : " + w.engineer.isBusy());
        System.out.println("   Rover busy     : " + w.rover.isBusy());
        System.out.println("   Repair robot busy: " + w.repairRobot.isBusy());
    }

    static void header(String title) {
        System.out.println("\n==================================================");
        System.out.println(title);
        System.out.println("==================================================");
    }

    public static void main(String[] args) {

        // ===================== SCENARIO 1 =====================
        header("SCENARIO 1: Successful Exploration");

        World w1 = buildWorld(20, 100);
        List<AbstractAgent> team1 = List.of(w1.scientist, w1.rover);
        ExplorationMission ex1 = new ExplorationMission("EX-1", "Olympus Mons", "Scientist", 5);

        printState("BEFORE", w1);
        OperationResult r1 = w1.colony.submitMission(ex1, team1);

        System.out.println("\n  Submission success : " + r1.isSuccess());
        System.out.println("  Message            : " + r1.getMessage());
        System.out.println("  Mission status     : " + ex1.getStatus());

        printState("DURING (after assignment)", w1);
        w1.colony.advanceTime(5);

        System.out.println("\n  Final mission status: " + ex1.getStatus());
        printState("AFTER", w1);

        // ===================== SCENARIO 2 =====================
        header("SCENARIO 2: Mission Rejection (Unsuitable Agent)");

        World w2 = buildWorld(20, 100);
        ResearchMission bad = new ResearchMission("M-BAD", w2.lab, 5);
        List<AbstractAgent> wrongTeam = List.of(w2.engineer);

        printState("BEFORE", w2);
        OperationResult r2 = w2.colony.submitMission(bad, wrongTeam);

        System.out.println("\n  Submission success : " + r2.isSuccess());
        System.out.println("  Reason             : " + r2.getMessage());
        System.out.println("  Mission status     : " + bad.getStatus());
        System.out.println("  Never became active: " + (w2.colony.getActiveMission() != bad));

        printState("AFTER", w2);

        // ===================== SCENARIO 3 =====================
        header("SCENARIO 3: Resource Shortage");

        World w3 = buildWorld(0, 100);
        List<AbstractAgent> team3 = List.of(w3.scientist, w3.rover);
        ExplorationMission ex3 = new ExplorationMission("EX-3", "Valles Marineris", "Scientist", 5);

        printState("BEFORE", w3);
        OperationResult r3 = w3.colony.submitMission(ex3, team3);

        System.out.println("\n  Submission success : " + r3.isSuccess());
        System.out.println("  Failure reason     : " + r3.getMessage());
        System.out.println("  Mission status     : " + ex3.getStatus());
        System.out.println("  Never became active: " + (w3.colony.getActiveMission() != ex3));

        printState("AFTER (resources must remain unchanged)", w3);

        // ===================== SCENARIO 4 =====================
        header("SCENARIO 4: Emergency Handling (Oxygen Leak)");

        World w4 = buildWorld(20, 100);
        OxygenLeak leak = new OxygenLeak("EM-004", w4.habitat, 20);

        System.out.println("  Emergency: Oxygen Leak");
        System.out.println("  Affected facility: " + w4.habitat.getName());
        System.out.println("  Oxygen before leak: " + w4.habitat.getResourceQuantity(ResourceType.OXYGEN));
        System.out.println("  Spare parts before repair: "
                + w4.colony.getResourceManager().getTotal(ResourceType.SPARE_PARTS));
        System.out.println("  Engineer available: " + w4.engineer.isAvailable());
        System.out.println("  Repair robot available: " + w4.repairRobot.isAvailable());

        OperationResult emergencyResult = leak.handle(w4.colony);

        System.out.println("\n  Emergency result: " + leak.getStatus());
        System.out.println("  Message: " + emergencyResult.getMessage());
        System.out.println("  Total oxygen lost: " + leak.getTotalOxygenLost());
        System.out.println("  Oxygen after emergency: " + w4.habitat.getResourceQuantity(ResourceType.OXYGEN));
        System.out.println("  Spare parts after repair: "
                + w4.colony.getResourceManager().getTotal(ResourceType.SPARE_PARTS));
        System.out.println("  Habitat leak active (after response): " + w4.habitat.hasOxygenLeak());

        if (leak.getStatus() == EmergencyStatus.UNRESOLVED) {
            System.out.println("  Failure reason: " + leak.getUnresolvedReason());
        }

        System.out.println("  Engineer busy after response: " + w4.engineer.isBusy());
        System.out.println("  Repair robot busy after response: " + w4.repairRobot.isBusy());
    }
}
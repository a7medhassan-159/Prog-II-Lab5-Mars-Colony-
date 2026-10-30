
import colony.*;
import missions.*;
import agents.*;
import Facilities.*;
import resources.*;
import java.util.*;
import environment.*;
import emergencies.*;

public class Main {

    // ---------------------------------------------------------------
    // Helper: stores everything belonging to one test world.
    // ---------------------------------------------------------------
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

    // ---------------------------------------------------------------
    // Build a fresh colony for each scenario.
    // ---------------------------------------------------------------
    static World buildWorld(int fuel, int oxygen) {

        World w = new World();

        w.colony = new MarsColony("Ares Base");

        w.lab = new Laboratory(
                "Central Lab",
                new SurfaceLocation(),
                10,
                true
        );

        w.power = new powerstation(
                "Solar Power Station",
                new SurfaceLocation(),
                20,
                true
        );

        w.storage = new Storagefacilities(
                "Main Storage",
                new SurfaceLocation(),
                50,
                true,
                ResourceType.FUEL,
                ResourceType.SPARE_PARTS
        );

        w.habitat = new habitat(
                "Main Habitat",
                new SurfaceLocation(),
                10,
                true
        );

        // Equipment.
        w.lab.addEquipment(
                new Equipment(
                        "EQ-01",
                        "Scientific Analyzer",
                        EquipmentType.SCIENTIFIC
                )
        );

        // Required for ExplorationMission.
        w.lab.addEquipment(
                new Equipment(
                        "EQ-02",
                        "Exploration Equipment",
                        EquipmentType.EXPLORATION
                )
        );

        // Resources.
        w.lab.addresource(ResourceType.WATER, 50);

        w.habitat.addresource(
                ResourceType.OXYGEN,
                oxygen
        );

        w.power.addresource(
                ResourceType.BATTERY_POWER,
                50
        );

        if (fuel > 0) {
            w.storage.addresource(
                    ResourceType.FUEL,
                    fuel
            );
        }

        w.storage.addresource(
                ResourceType.SPARE_PARTS,
                10
        );

        // Register facilities.
        w.colony.addFacility(w.lab);
        w.colony.addFacility(w.power);
        w.colony.addFacility(w.storage);
        w.colony.addFacility(w.habitat);

        // Create agents.
        w.scientist = new Scientist(
                "C-01",
                "Dr. Sarah",
                w.lab
        );

        w.engineer = new Engineer(
                "C-02",
                "Eng. Alex",
                w.lab
        );

        w.rover = new ExplorationRover(
                "R-01",
                "Rover Alpha",
                w.lab,
                100
        );

        w.repairRobot = new RepairRobot(
                "R-02",
                "Repair Robot",
                w.lab,
                100
        );

        // Register agents.
        w.colony.addCrewMember(w.scientist);
        w.colony.addCrewMember(w.engineer);

        w.colony.addRobot(w.rover);
        w.colony.addRobot(w.repairRobot);

        return w;
    }

    // ---------------------------------------------------------------
    // Print colony state.
    // ---------------------------------------------------------------
    static void printState(String label, World w) {

        System.out.println("\n  [" + label + "]");

        System.out.println("   Active mission : "
                + (w.colony.getActiveMission() == null
                ? "none"
                : w.colony.getActiveMission().getId()));

        System.out.println("   Resources      : "
                + w.colony.getResourceManager().getTotals());

        System.out.println("   Scientist busy : "
                + w.scientist.isBusy());

        System.out.println("   Engineer busy  : "
                + w.engineer.isBusy());

        System.out.println("   Rover busy     : "
                + w.rover.isBusy());

        System.out.println("   Repair robot busy: "
                + w.repairRobot.isBusy());
    }

    // ---------------------------------------------------------------
    // Print scenario header.
    // ---------------------------------------------------------------
    static void header(String title) {

        System.out.println(
                "\n=================================================="
        );

        System.out.println(title);

        System.out.println(
                "=================================================="
        );
    }

    // ---------------------------------------------------------------
    // MAIN
    // ---------------------------------------------------------------
    public static void main(String[] args) {

        // ============================================================
        // SCENARIO 1: Successful Exploration
        // ============================================================
        header("SCENARIO 1: Successful Exploration");

        World w1 = buildWorld(20, 100);

        List<AbstractAgent> team1 =
                List.of(w1.scientist, w1.rover);

        ExplorationMission ex1 =
                new ExplorationMission(
                        "EX-1",
                        "Olympus Mons",
                        "Scientist",
                        5
                );

        printState("BEFORE", w1);

        OperationResult r1 =
                w1.colony.submitMission(ex1, team1);

        System.out.println("\n  Submission success : "
                + r1.isSuccess());

        System.out.println("  Message            : "
                + r1.getMessage());

        System.out.println("  Mission status     : "
                + ex1.getStatus());

        printState("DURING (after assignment)", w1);

        w1.colony.advanceTime(5);

        System.out.println("\n  Final mission status: "
                + ex1.getStatus());

        printState("AFTER", w1);


        // ============================================================
        // SCENARIO 2: Mission Rejection (Unsuitable Agent)
        // ============================================================
        header("SCENARIO 2: Mission Rejection (Unsuitable Agent)");

        World w2 = buildWorld(20, 100);

        ResearchMission bad =
                new ResearchMission(
                        "M-BAD",
                        w2.lab,
                        5
                );

        List<AbstractAgent> wrongTeam =
                List.of(w2.engineer);

        printState("BEFORE", w2);

        OperationResult r2 =
                w2.colony.submitMission(bad, wrongTeam);

        System.out.println("\n  Submission success : "
                + r2.isSuccess());

        System.out.println("  Reason             : "
                + r2.getMessage());

        System.out.println("  Mission status     : "
                + bad.getStatus());

        System.out.println("  Never became active: "
                + (w2.colony.getActiveMission() != bad));

        printState("AFTER", w2);


        // ============================================================
        // SCENARIO 3: Resource Shortage
        // ============================================================
        header("SCENARIO 3: Resource Shortage");

        // No fuel is available in this colony.
        World w3 = buildWorld(0, 100);

        List<AbstractAgent> team3 =
                List.of(w3.scientist, w3.rover);

        ExplorationMission ex3 =
                new ExplorationMission(
                        "EX-3",
                        "Valles Marineris",
                        "Scientist",
                        5
                );

        printState("BEFORE", w3);

        OperationResult r3 =
                w3.colony.submitMission(ex3, team3);

        System.out.println("\n  Submission success : "
                + r3.isSuccess());

        System.out.println("  Failure reason     : "
                + r3.getMessage());

        System.out.println("  Mission status     : "
                + ex3.getStatus());

        System.out.println("  Never became active: "
                + (w3.colony.getActiveMission() != ex3));

        printState(
                "AFTER (resources must remain unchanged)",
                w3
        );


        // ============================================================
        // SCENARIO 4: Emergency Handling (Oxygen Leak)
        // ============================================================
        header("SCENARIO 4: Emergency Handling (Oxygen Leak)");

        World w4 = buildWorld(20, 100);

        EmergencyManager emergencyManager =
                new EmergencyManager(w4.colony);

        System.out.println("  Emergency: Oxygen Leak");

        System.out.println("  Affected facility: "
                + w4.habitat.getName());

        System.out.println("  Oxygen before leak: "
                + w4.habitat.getResourceQuantity(
                        ResourceType.OXYGEN));

        // Use colony-wide total, not the local resource quantity.
        System.out.println("  Spare parts before repair: "
                + w4.colony.getResourceManager().getTotal(
                        ResourceType.SPARE_PARTS));

        System.out.println("  Engineer available: "
                + w4.engineer.isAvailable());

        System.out.println("  Repair robot available: "
                + w4.repairRobot.isAvailable());

        System.out.println("  Habitat leak before: "
                + w4.habitat.hasOxygenLeak());

        // Handle the oxygen leak.
        EmergencyManager.Status emergencyResult =
                emergencyManager.handleOxygenLeak(
                        "EM-004",
                        w4.habitat,
                        20,
                        2,
                        true
                );

        System.out.println("\n  Emergency result: "
                + emergencyResult);

        System.out.println("  Oxygen after emergency: "
                + w4.habitat.getResourceQuantity(
                        ResourceType.OXYGEN));

        System.out.println("  Spare parts after repair: "
                + w4.colony.getResourceManager().getTotal(
                        ResourceType.SPARE_PARTS));

        System.out.println("  Habitat leak active: "
                + w4.habitat.hasOxygenLeak());

        System.out.println("  Failure reason: "
                + emergencyManager.getLastReason());

        System.out.println("  Engineer busy after response: "
                + w4.engineer.isBusy());

        System.out.println("  Repair robot busy after response: "
                + w4.repairRobot.isBusy());


        // ============================================================
        // SCENARIO 5: Environmental Effect (Dust Storm)
        // ============================================================
        header("SCENARIO 5: Environmental Effect (Dust Storm)");

        World w5 = buildWorld(20, 100);

        List<AbstractAgent> team5 =
                List.of(w5.scientist, w5.rover);

        ExplorationMission ex5 =
                new ExplorationMission(
                        "EX-5",
                        "Olympus Mons",
                        "Scientist",
                        5
                );

        // Activate dust storm.
        w5.colony.activateCondition(
                EnvironmentCondition.DUST_STORM
        );

        System.out.println("  Environment: "
                + w5.colony.getEnvironment());

        OperationResult r5 =
                w5.colony.submitMission(ex5, team5);

        System.out.println("  Exploration Mission started: "
                + r5.isSuccess());

        if (!r5.isSuccess()) {

            System.out.println(
                    "  Exploration Mission cannot start."
            );

            System.out.println("  Reason: "
                    + r5.getMessage());
        }

        System.out.println("  Mission status: "
                + ex5.getStatus());

        System.out.println("  Never became active: "
                + (w5.colony.getActiveMission() != ex5));


        // ============================================================
        // FINISH
        // ============================================================
        System.out.println(
                "\n=================================================="
        );

        System.out.println(
                "             ALL 5 SCENARIOS EXECUTED"
        );

        System.out.println(
                "=================================================="
        );
    }
}

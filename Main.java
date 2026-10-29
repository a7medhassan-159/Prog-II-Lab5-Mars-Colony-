import colony.*;
import missions.*;
import agents.*;
import Facilities.*;
import resources.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("    MARS COLONY - MISSION TEST CASES (NO EMERGENCY)");
        System.out.println("==================================================\n");

        // 1. إنشاء المستعمرة
        MarsColony colony = new MarsColony("Ares Base");

        // 2. إنشاء المنشآت الأصلية وتزويدها بالموارد والمعدات
        Laboratory lab = new Laboratory("Central Lab", new SurfaceLocation(), 10, true);
        powerstation power = new powerstation("Solar Power Station", new SurfaceLocation(), 20, true);
        Storagefacilities storage = new Storagefacilities("Main Storage", new SurfaceLocation(), 50, true);

        // إضافة المعدات العلمية المطلوبة بداخل المعمل (id, name, type)
        lab.addEquipment(new Equipment("EQ-01", "Scientific Analyzer", EquipmentType.SCIENTIFIC));

        // شحن المنشآت بالموارد المخصصة لها ليتعرف عليها الـ Resource Manager
        lab.addresource(ResourceType.WATER, 50);
        power.addresource(ResourceType.BATTERY_POWER, 50);
        storage.addresource(ResourceType.FUEL, 20);

        // تسجيل المنشآت بداخل المستعمرة
        colony.addFacility(lab);
        colony.addFacility(power);
        colony.addFacility(storage);

        // 3. إضافة الأفراد والروبرتات من الكلاسات المتاحة بالمشروع مباشرة (Scientist, Engineer, ExplorationRover)
        Scientist scientist = new Scientist("C-01", "Dr. Sarah", lab);
        Engineer engineer = new Engineer("C-02", "Eng. Alex", lab);
        ExplorationRover rover = new ExplorationRover("R-01", "Rover Alpha", lab, 100);

        colony.addCrewMember(scientist);
        colony.addCrewMember(engineer);
        colony.addRobot(rover);

        List<AbstractAgent> researchTeam = List.of(scientist);

        System.out.println("--- Initial Colony State ---");
        System.out.println("Active Mission: " + colony.getActiveMission());
        System.out.println("Total Colony Resources: " + colony.getResourceManager().getTotals());
        System.out.println("--------------------------------------------------\n");

        // ====================================================================
        // TEST CASE 1: Successful Mission Submission and Execution (Points 1 - 8)
        // ====================================================================
        System.out.println("-------------[TEST 1] Successful Mission Flow (ResearchMission) -----------------");

        // إنشاء مهمة بحث علمي تدوم 5 وحدات زمنية
        ResearchMission mission1 = new ResearchMission("M1", lab, 5);

        // 1 & 5. تقديم المهمة والتحقق من القبول
        OperationResult result1 = colony.submitMission(mission1, researchTeam);
        System.out.println("1 & 5. Submission Result: " + result1.getMessage());

        // 2. التأكد من وجود مهمة نشطة بداخل المستعمرة
        System.out.println("2. Is Active Mission Present? " + (colony.getActiveMission() != null));
        System.out.println("   Active Mission ID: " + colony.getActiveMission().getId());

        // 3. فحص إتاحة وانشغال الأفراد (Busy Status)
        System.out.println("3. Agent " + scientist.getName() + " Busy Status: " + scientist.isBusy());

        // 4. فحص الموارد المتبقية بعد التقديم والخصم
        System.out.println("4. Resources After Assignment: " + colony.getResourceManager().getTotals());

        // 6. تحديث حالة المهمة (تصبح ASSIGNED عند القبول)
        System.out.println("6. Mission Status: " + mission1.getStatus());

        // 7. تقديم زمن المحاكاة (المستعمرة تبدأ المهمة وتتقدم بها زمنيًا)
        System.out.println("\n--- 7. Advancing Simulation Time (T+5) ---");
        colony.advanceTime(5);

        // 8. اكتمال المهمة وتحرير الأفراد تلقائياً
        System.out.println("8. Final Mission Status: " + mission1.getStatus());
        System.out.println("8. Agent Released Status (isBusy): " + scientist.isBusy());
        System.out.println("8. Active Mission Cleared: " + (colony.getActiveMission() == null));
        System.out.println("--------------------------------------------------\n");

        // ====================================================================
        // TEST CASE 2: Alternative Flow - Rejection due to Active Mission Conflict
        // ====================================================================
        System.out.println(">>> [TEST 2] Alternative Flow: Rejection (Active Mission Exists) <<<");

        ResearchMission activeMission = new ResearchMission("M2", lab, 10);
        ResearchMission rejectedMission = new ResearchMission("M3", lab, 3);

        // تقديم المهمة M2 لتصبح هي المهمة النشطة
        colony.submitMission(activeMission, researchTeam);
        System.out.println("Current Active Mission: " + colony.getActiveMission().getId());

        // محاولة تقديم المهمة M3 أثناء وجود مهمة نشطة بالفعالية
        OperationResult resConflict = colony.submitMission(rejectedMission, researchTeam);

        // طباعة سبب الرفض وإثبات أنها لم تتفعل إطلاقاً
        System.out.println("Submission Success: " + resConflict.isSuccess());
        System.out.println("Failure Reason: " + resConflict.getMessage());
        System.out.println("Rejected Mission Status: " + rejectedMission.getStatus());
        System.out.println("CONFIRMED: Rejected Mission Never Became Active -> " + (colony.getActiveMission() != rejectedMission));
        System.out.println("--------------------------------------------------\n");

        // ====================================================================
        // TEST CASE 3: Alternative Flow - Rejection due to Resource Shortage / Preconditions
        // ====================================================================
        System.out.println(">>> [TEST 3] Alternative Flow: Rejection (Missing Preconditions / Shortage) <<<");

        // إنهاء المهمة النشطة الحالية لتفريغ الـ Active Mission
        colony.advanceTime(10);

        // إنشاء مهمة استكشاف تتطلب معدات استكشاف ووقود غير متوفرين بالكمية الكافية
        ExplorationMission heavyMission = new ExplorationMission("M4", "Olympus Mons", "Pilot", 5);
        OperationResult resShortage = colony.submitMission(heavyMission, researchTeam);

        System.out.println("Submission Success: " + resShortage.isSuccess());
        System.out.println("Failure Reason: " + resShortage.getMessage());
        System.out.println("M4 Mission Status: " + heavyMission.getStatus());
        System.out.println("CONFIRMED: M4 Never Became Active -> " + (colony.getActiveMission() != heavyMission));

        System.out.println("\n==================================================");
        System.out.println("             ALL TEST CASES COMPLETED             ");
        System.out.println("==================================================");
    }
}
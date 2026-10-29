package colony;
import java.util.*;

public class Reacordingevents {

    private final List<String> entries = new ArrayList<>();

    public void record(int time, String message) {
        String line = "[T=" + time + "] " + message;
        entries.add(line);
        System.out.println(line);
    }

    public List<String> getEntries() {
        return entries;
    }
}
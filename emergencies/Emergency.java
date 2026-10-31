package emergencies;

import colony.*;
import agents.*;
import resources.*;

import java.util.*;


public abstract class Emergency {

    private final String id;
    private final String description;
    private EmergencyStatus status = EmergencyStatus.PENDING;
    private String unresolvedReason;
    private final List<AbstractAgent> responders = new ArrayList<>();

    protected Emergency(String id, String description) {
        this.id = id;
        this.description = description;
    }


    public abstract List<Capability> getRequiredResponders();

    protected abstract Resources getResponseResources();

    public abstract Location getLocation();

    protected abstract void onDetected(MarsColony colony);

    protected abstract void onResolved(MarsColony colony);

    protected abstract void onUnresolved(MarsColony colony);

    protected String checkPreconditions(MarsColony colony) {
        return null;
    }

    protected boolean isExcludedResponder(AbstractAgent agent) {
        return false;
    }


    public final OperationResult handle(MarsColony colony) {
        if (status != EmergencyStatus.PENDING) {
            System.out.println("Emergency " + id + " was already handled (" + status
                    + "); unresolved emergencies are not retried automatically");
        }
        status = EmergencyStatus.ACTIVE;
        colony.log("EMERGENCY " + id + ": " + description);

        onDetected(colony);
        colony.reevaluateActiveMission();

        List<AbstractAgent> chosen = new ArrayList<>();
        String reason = checkPreconditions(colony);
        if (reason == null) {
            reason = findResponders(colony, chosen);
        }
        if (reason == null) {
            String shortage = colony.getResourceManager().findShortage(getResponseResources());
            if (shortage != null) {
                reason = "Insufficient response resources: " + shortage;
            }
        }
        if (reason != null) {
            return markUnresolved(colony, reason);
        }

        Map<AbstractAgent, Location> origins = new LinkedHashMap<>();
        for (AbstractAgent a : chosen) {
            origins.put(a, a.getLocation());
            a.setLocation(getLocation());
            a.setBusy(true);
        }
        responders.addAll(chosen);
        colony.log("  Responders dispatched to " + getLocation().getName() + ": " + chosen);

        colony.getResourceManager().consume(getResponseResources());
        colony.log("  Response resources consumed: " + getResponseResources());

        onResolved(colony);
        status = EmergencyStatus.RESOLVED;

        for (AbstractAgent a : chosen) {
            a.setBusy(false);
            if (a.isOperational()) {
                a.setLocation(origins.get(a));
            }
        }
        colony.log("  Emergency " + id + " RESOLVED; responders released");
        return OperationResult.success("Emergency " + id + " resolved");
    }

    private String findResponders(MarsColony colony, List<AbstractAgent> out) {
        for (Capability needed : getRequiredResponders()) {
            AbstractAgent found = null;
            for (AbstractAgent a : colony.getAllAgents()) {
                if (!out.contains(a) && !isExcludedResponder(a) && a.isAvailable()
                        && a.hasCapability(needed) && a.canHandle(this)) {
                    found = a;
                    break;
                }
            }
            if (found == null) {
                return "No available responder with capability " + needed
                        + " (all candidates are busy, unfit or missing)";
            }
            out.add(found);
        }
        return null;
    }

    private OperationResult markUnresolved(MarsColony colony, String reason) {
        status = EmergencyStatus.UNRESOLVED;
        unresolvedReason = reason;
        onUnresolved(colony);
        colony.log("  Emergency " + id + " UNRESOLVED. Reason: " + reason
                + " (no response resources consumed, no automatic retry)");
        return OperationResult.failure("Emergency " + id + " UNRESOLVED: " + reason);
    }


    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public EmergencyStatus getStatus() {
        return status;
    }

    public String getUnresolvedReason() {
        return unresolvedReason;
    }

    public List<AbstractAgent> getResponders() {
        return Collections.unmodifiableList(responders);
    }

    public String toString() {
        return "[" + id + "] " + description + " (" + status
                + (unresolvedReason != null ? ", reason: " + unresolvedReason : "") + ")";
    }
}

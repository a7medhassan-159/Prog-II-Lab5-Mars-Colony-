package resources;

import java.util.*;

import Facilities.Facilities;

public class Resources {

    private final Map<ResourceType, Integer> amounts = new EnumMap<>(ResourceType.class);

    private List<Facilities> facilities = new ArrayList<>();


    public Resources() {
    }

    
    public Resources(List<Facilities> facilities) {
        if (facilities != null) {
            this.facilities = facilities;
        }
    }


    public int getQuantity(ResourceType type) {
        return amounts.getOrDefault(type, 0);
    }


    public void addQuantity(ResourceType type, int amount) {
        if (amount > 0) {
            amounts.merge(type, amount, Integer::sum);
        }
    }

    public boolean consume(ResourceType type, int amount) {
        int currentQuantity = getQuantity(type);
        if (amount > 0 && amount <= currentQuantity) {
            amounts.put(type, currentQuantity - amount);
            return true;
        }
        return false;
    }

    public Set<ResourceType> getTypes() {
        return amounts.keySet();
    }

    public boolean isEmpty() {
        return amounts.isEmpty();
    }

    public Map<ResourceType, Integer> asMap() {
        return amounts;
    }


    
    public int getTotal(ResourceType type) {
        int total = 0;
        for (Facilities f : facilities) {
            if (f.isOperational()) {
                total += f.getResourceQuantity(type);
            }
        }
        return total;
    }
    
    public Map<ResourceType, Integer> getTotals() {
        Map<ResourceType, Integer> totals = new EnumMap<>(ResourceType.class);
        for (ResourceType t : ResourceType.values()) {
            totals.put(t, getTotal(t));
        }
        return totals;
    }

    public String findShortage(Resources requirement) {
        String error = "";

        for (ResourceType type : requirement.getTypes()) {
            int requiredAmount = requirement.getQuantity(type);
            int availableAmount = getTotal(type);

            if (availableAmount < requiredAmount) {
                if (!error.isEmpty()) {
                    error += "-- ";
                }
                error += "insufficient " + type + " (required " + requiredAmount + ", available " + availableAmount + ")";
            }
        }

        if (error.isEmpty()) {
            return null;
        }
        return error;
    }

    public boolean canSupply(Resources requirement) {
        return findShortage(requirement) == null;
    }

    private int draw(ResourceType type, int amount) {
        int remaining = amount;

        for (Facilities f : facilities) {
            if (remaining == 0) {
                break;
            }

            if (f.isOperational()) {
                int available = f.getResourceQuantity(type);

                if (available > 0) {
                    int toTake = remaining;
                    if (available < remaining) {
                        toTake = available;
                    }

                    f.consumeresource(type, toTake);
                    remaining = remaining - toTake;
                }
            }
        }

        return amount - remaining;
    }

    public boolean consume(Resources requirement) {
        if (!canSupply(requirement)) {
            return false;
        }

        for (ResourceType type : requirement.getTypes()) {
            int amount = requirement.getQuantity(type);
            draw(type, amount);
        }

        return true;
    }


      
    public String toString() {
        if (isEmpty()) {
            return "none";
        }

        String text = "";
        for (ResourceType type : amounts.keySet()) {
            if (!text.isEmpty()) {
                text += ", ";
            }
            text += type + " x" + amounts.get(type);
        }
        return text;
    }
}

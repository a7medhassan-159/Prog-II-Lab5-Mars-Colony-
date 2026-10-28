package resources;

import  colony.*;
import java.util.*;
import agents.*;

public class Resources {
    private final ResourceType type;
    private int quantity;
   
    public Resources(ResourceType type, int quantity) {
        this.type = type;
        this.quantity = quantity;
        
    }
    public ResourceType getType() {
        return type;
    }
    public int getQuantity() {
        return quantity;
    }
    
    public void addQuantity(int amount) {
        this.quantity += amount;
    }
    public boolean consume(int amount) {
        if (amount <= quantity) {
            quantity -= amount;
            return true;
        } else {
            return false;
        }
    }
    @Override
    public String toString() {
        return "Resources [type=" + type + ", quantity=" + quantity + ", unit=" + unit + "]";
    }
    
}

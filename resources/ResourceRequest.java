package resources;
public class ResourceRequest {
    private final ResourceType type;
    private final int quantity;

    public ResourceRequest(ResourceType type, int quantity) {
        if (type == null) throw new IllegalArgumentException("Resource type is required.");
        if (quantity <= 0) throw new IllegalArgumentException("Requested quantity must be positive.");
        this.type = type;
        this.quantity = quantity;
    }

    
    public ResourceType getType() { return type; }
    public int getQuantity() { return quantity; }
}

package colony;


public final class OperationResult {

    private final boolean success;
    private final String message;

    private OperationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static OperationResult success(String message) {
        return new OperationResult(true, message);
    }

    public static OperationResult failure(String reason) {
        return new OperationResult(false, reason);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    
    public String toString() {
        return (success ? "SUCCESS: " : "FAILURE: ") + message;
    }
}

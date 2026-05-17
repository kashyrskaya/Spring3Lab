package lt.esdc.exception;

public class StoreIsFullException extends RuntimeException {
    public StoreIsFullException(String message) {
        super(message);
    }
}
package lt.esdc.exception;

public class AlchemistNotFoundException extends RuntimeException {
    public AlchemistNotFoundException(String message) {
        super(message);
    }
}
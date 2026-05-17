package lt.esdc.exception;

public class AlchemistAlreadyExistsException extends RuntimeException {
    public AlchemistAlreadyExistsException(String message) {
        super(message);
    }
}
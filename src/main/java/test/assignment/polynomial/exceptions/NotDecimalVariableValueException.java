package test.assignment.polynomial.exceptions;

public class NotDecimalVariableValueException extends RuntimeException {
    public NotDecimalVariableValueException() {
        super();
    }

    public NotDecimalVariableValueException(String message) {
        super(message);
    }

    public NotDecimalVariableValueException(String message, Throwable cause) {
        super(message, cause);
    }
}

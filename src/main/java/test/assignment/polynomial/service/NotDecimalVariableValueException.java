package test.assignment.polynomial.service;

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

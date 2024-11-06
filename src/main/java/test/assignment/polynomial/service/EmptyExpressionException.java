package test.assignment.polynomial.service;

public class EmptyExpressionException extends RuntimeException {
    public EmptyExpressionException() {
        super();
    }

    public EmptyExpressionException(String message) {
        super(message);
    }

    public EmptyExpressionException(String message, Throwable cause) {
        super(message, cause);
    }
}

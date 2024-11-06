package test.assignment.polynomial.service;

public class MalformedExpressionException extends RuntimeException {
    public MalformedExpressionException() {
        super();
    }

    public MalformedExpressionException(String message) {
        super(message);
    }

    public MalformedExpressionException(String message, Throwable cause) {
        super(message, cause);
    }
}

package test.assignment.polynomial.exceptions;

public class MalformedExpressionException extends RuntimeException {
    public MalformedExpressionException() {
        super();
    }

    public MalformedExpressionException(String message) {
        super(message);
    }
}

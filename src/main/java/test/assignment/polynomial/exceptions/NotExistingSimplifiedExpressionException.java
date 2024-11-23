package test.assignment.polynomial.exceptions;

public class NotExistingSimplifiedExpressionException extends RuntimeException {
    public NotExistingSimplifiedExpressionException() {
        super();
    }

    public NotExistingSimplifiedExpressionException(String message) {
        super(message);
    }

    public NotExistingSimplifiedExpressionException(String message, Throwable cause) {
        super(message, cause);
    }
}

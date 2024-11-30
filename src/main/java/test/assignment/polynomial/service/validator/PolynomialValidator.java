package test.assignment.polynomial.service.validator;


public interface PolynomialValidator {
    void validateExpressionString(String expression);
    double validateVariableValue(String x);
}

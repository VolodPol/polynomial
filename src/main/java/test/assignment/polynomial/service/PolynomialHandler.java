package test.assignment.polynomial.service;

public interface PolynomialHandler {
    String simplify(String raw);
    double evaluate(String simplified, double value);
}

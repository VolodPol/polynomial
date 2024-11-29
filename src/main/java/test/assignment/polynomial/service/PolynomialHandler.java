package test.assignment.polynomial.service;

import test.assignment.polynomial.entity.SimplifiedExpression;

public interface PolynomialHandler {
    SimplifiedExpression simplify(String raw);
    double evaluate(String simplified, String value);
}

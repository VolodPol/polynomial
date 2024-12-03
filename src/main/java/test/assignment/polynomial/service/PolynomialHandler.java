package test.assignment.polynomial.service;

import test.assignment.polynomial.dto.ExpressionDto;
import test.assignment.polynomial.entity.SimplifiedExpression;

public interface PolynomialHandler {
    SimplifiedExpression simplify(ExpressionDto raw);
    double evaluate(ExpressionDto simplified, String value);
}

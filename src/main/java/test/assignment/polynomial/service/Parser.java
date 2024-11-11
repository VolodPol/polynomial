package test.assignment.polynomial.service;

import test.assignment.polynomial.service.domain.Expression;

public interface Parser {
    Expression parseExpression(String representation);

    String exressionToString(Expression expression);
}

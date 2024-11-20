package test.assignment.polynomial.service;

import test.assignment.polynomial.service.domain.Expression;

public interface ExpressionParser {
    Expression parseExpression(String representation);

    String expressionToString(Expression expression);
}

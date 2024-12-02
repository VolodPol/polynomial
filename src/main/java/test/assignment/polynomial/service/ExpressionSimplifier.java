package test.assignment.polynomial.service;

import test.assignment.polynomial.service.domain.Expression;

public abstract class ExpressionSimplifier {

    public void simplifyExpression(Expression expression) {
        simplifyMultipliers(expression);
        applyProduct(expression);
        simplifyMultipliers(expression);
    }

    protected abstract void simplifyMultipliers(Expression expression);

    protected abstract void applyProduct(Expression expression);
}

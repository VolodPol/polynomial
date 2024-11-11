package test.assignment.polynomial.service;

import org.springframework.stereotype.Service;
import test.assignment.polynomial.service.domain.Expression;

@Service
public class ExpressionParser implements Parser {
    @Override
    public Expression parseExpression(String representation) {
        return null;
    }

    @Override
    public String exressionToString(Expression expression) {
        return "";
    }
}

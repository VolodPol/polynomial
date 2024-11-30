package test.assignment.polynomial.service.validator.impl;

import test.assignment.polynomial.exceptions.EmptyExpressionException;
import test.assignment.polynomial.exceptions.MalformedExpressionException;
import test.assignment.polynomial.service.validator.BaseValidator;


public class ContentValidator extends BaseValidator {

    @Override
    public void validateExpressionString(String expression) {
        if (expression.isBlank())
            throw new EmptyExpressionException("The expression must not be empty!");

        if (!expression.matches("[\\d-+*^(%s)]+".formatted(VARIABLE)))
            throw new MalformedExpressionException("The expression contains forbidden characters!");
    }
}

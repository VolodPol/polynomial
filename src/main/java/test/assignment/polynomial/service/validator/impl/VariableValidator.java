package test.assignment.polynomial.service.validator.impl;

import test.assignment.polynomial.exceptions.MalformedExpressionException;
import test.assignment.polynomial.service.validator.BaseValidator;
import test.assignment.polynomial.service.validator.PolynomialValidator;

public class VariableValidator extends BaseValidator {

    private final PolynomialValidator validator;

    public VariableValidator(PolynomialValidator validator) {
        this.validator = validator;
    }

    @Override
    public void validateExpressionString(String expression) {
        validator.validateExpressionString(expression);
        checkX(expression);
    }

    private void checkX(String expression) {
        for (int i = 1; i < expression.length() - 1; i++) {
            if (expression.charAt(i) == VARIABLE) {
                String leftSubstring = expression.substring(0, i);
                if (expression.charAt(i - 1) != OPENING_BRACKET && !leftSubstring.matches(".*\\d+[+\\-*]$"))
                    throw new MalformedExpressionException("Malformed characters before 'x' variable");

                String rightSubstring = expression.substring(i + 1);
                if (expression.charAt(i + 1) != CLOSING_BRACKET && !rightSubstring.matches("^[+\\-*]\\d+.*") && !rightSubstring.matches("^\\^\\d+.*"))
                    throw new MalformedExpressionException("Malformed characters after 'x' variable");

            }
        }
    }
}

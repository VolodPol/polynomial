package test.assignment.polynomial.service.validator;

import test.assignment.polynomial.exceptions.EmptyValueException;
import test.assignment.polynomial.exceptions.NotDecimalVariableValueException;

public abstract class BaseValidator implements PolynomialValidator {
    protected final char VARIABLE = 'x';

    @Override
    public double validateVariableValue(String x) {
        if (x == null || x.isBlank())
            throw new EmptyValueException("Empty variable");

        try {
            return Double.parseDouble(x);
        } catch (NumberFormatException e){
            throw new NotDecimalVariableValueException("The value must be a decimal number!");
        }
    }
}

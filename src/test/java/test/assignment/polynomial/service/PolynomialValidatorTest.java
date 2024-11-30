package test.assignment.polynomial.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.assignment.polynomial.config.ValidationConfiguration;
import test.assignment.polynomial.exceptions.EmptyExpressionException;
import test.assignment.polynomial.exceptions.MalformedExpressionException;
import test.assignment.polynomial.exceptions.NotDecimalVariableValueException;
import test.assignment.polynomial.service.validator.PolynomialValidator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ValidationConfiguration.class)
class PolynomialValidatorTest {
    @Autowired
    private PolynomialValidator polynomialValidator;

    @Test
    void emptyInputShouldBeInvalid() {
        String invalidExpression = "";
        assertThrows(EmptyExpressionException.class, () -> polynomialValidator.validateExpressionString(invalidExpression));
    }

    @Test
    void absentAsteriskForProductInvalid() {
        assertThrows(MalformedExpressionException.class, () -> polynomialValidator.validateExpressionString("2x^2"));
        assertThrows(MalformedExpressionException.class, () -> polynomialValidator.validateExpressionString("(2*x+3)(3*x^2-1)"));
    }

    @Test
    void validExpression() {
        assertDoesNotThrow(
                () -> polynomialValidator.validateExpressionString("(2*x-7)*(3*x^2-x+1)")
        );
    }

    @Test
    void invalidDoubleValue() {
        assertThrows(EmptyExpressionException.class, () -> polynomialValidator.validateVariableValue(""));
        assertThrows(NotDecimalVariableValueException.class, () -> polynomialValidator.validateVariableValue("a"));
    }

    @Test
    void validDoubleValue() {
        assertDoesNotThrow(() -> polynomialValidator.validateVariableValue("7"));
    }
}
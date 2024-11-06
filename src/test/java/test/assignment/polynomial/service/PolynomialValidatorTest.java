package test.assignment.polynomial.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PolynomialValidator.class)
class PolynomialValidatorTest {
    @Autowired
    private Validator validator;

    @Test
    void emptyInputShouldBeInvalid() {
        String invalidExpression = "";
        assertThrows(EmptyExpressionException.class, () -> validator.validateExpressionString(invalidExpression));
    }

    @Test
    void absentAsteriskForProductInvalid() {
        assertThrows(MalformedExpressionException.class, () -> validator.validateExpressionString("2x^2"));
        assertThrows(MalformedExpressionException.class, () -> validator.validateExpressionString("(2*x+3)(3*x^2-1)"));
    }

    @Test
    void validExpression() {
        assertDoesNotThrow(
                () -> validator.validateExpressionString("(2*x-7)*(3*x^2-x+1)")
        );
    }
}
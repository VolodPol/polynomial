package test.assignment.polynomial.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.assignment.polynomial.service.domain.Expression;
import test.assignment.polynomial.service.domain.Expression.Polynomial;
import test.assignment.polynomial.service.domain.Expression.Polynomial.Additive;
import test.assignment.polynomial.service.impl.ExpressionSimplifierImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ExpressionSimplifierImpl.class)
class ExpressionSimplifierTest {
    @Autowired
    private ExpressionSimplifier simplifier;

    @Test
    void simplifyRegular() {
        Expression raw = new Expression(new ArrayList<>(List.of(
                new Polynomial(
                        new ArrayList<>(List.of(
                                new Additive(2, 1),
                                new Additive(7, 0)
                        ))
                ),
                new Polynomial(
                        new ArrayList<>(List.of(
                                new Additive(3, 2),
                                new Additive(-1, 1),
                                new Additive(1, 0)
                        ))
                )
        )));

        Expression expected = new Expression(List.of(
                new Polynomial(new ArrayList<>(List.of(
                        new Additive(7, 0),
                        new Additive(-5, 1),
                        new Additive(19, 2),
                        new Additive(6, 3)
                )))
        ));
        simplifier.simplifyExpression(raw);

        assertEquals(expected, raw);
    }

    @Test
    void simplifyMultiplication() {
        Expression raw = new Expression(new ArrayList<>(List.of(
                new Polynomial(
                        new ArrayList<>(List.of(
                                new Additive(1, 0),
                                new Additive(4, 1),
                                new Additive(-2, 0),
                                new Additive(2, 1),
                                new Additive(3, 2),
                                new Additive(-1, 2),
                                new Additive(4, 4),
                                new Additive(3, 2),
                                new Additive(5, 3)

                        ))
                )
        )));

        Expression expected = new Expression(List.of(
                new Polynomial(
                        new ArrayList<>(List.of(
                                new Additive(-1, 0),
                                new Additive(6, 1),
                                new Additive(5, 2),
                                new Additive(5, 3),
                                new Additive(4, 4)
                        ))
                )
        ));
        simplifier.simplifyExpression(raw);

        assertEquals(expected, raw);
    }
}
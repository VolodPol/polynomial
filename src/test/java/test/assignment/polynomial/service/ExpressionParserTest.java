package test.assignment.polynomial.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.assignment.polynomial.service.domain.Expression;
import test.assignment.polynomial.service.domain.Expression.Polynomial;
import test.assignment.polynomial.service.domain.Expression.Polynomial.Additive;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ExpressionParser.class})
class ExpressionParserTest {

    @Autowired
    private ExpressionParser parser;

    @Test
    void parseSimpleExpression() {
        String input = "3*x-8";

        var firstAdditive = new Additive(3, 1);
        var secondAdditive = new Additive(-8, 0);
        Expression expected = new Expression(new ArrayList<>(List.of(
                new Polynomial(new ArrayList<>(List.of(
                        firstAdditive,
                        secondAdditive
        ))))));
        assertEquals(expected, parser.parseExpression(input));
    }

    @Test
    void simpleExpressionToString() {
        String expected = "3*x-8";

        var firstAdditive = new Additive(3, 1);
        var secondAdditive = new Additive(-8, 0);
        Expression input = new Expression(new ArrayList<>(List.of(
                new Polynomial(new ArrayList<>(List.of(
                        firstAdditive,
                        secondAdditive
                ))))));
        assertEquals(expected, parser.exressionToString(input));
    }

    @Test
    void parseRegularExpression() {
        String input = "(2*x+7)*(3*x^2-x+1)";

        var firstAdditive = new Additive(2, 1);
        var secondAdditive = new Additive(7, 0);

        var thirdAdditive = new Additive(3, 2);
        var fourthAdditive = new Additive(-1, 1);
        var fifthAdditive = new Additive(1, 0);

        Expression expected = new Expression(new ArrayList<>(List.of(
                new Polynomial(new ArrayList<>(List.of(
                        firstAdditive,
                        secondAdditive
                ))),
                new Polynomial(new ArrayList<>(List.of(
                        thirdAdditive,
                        fourthAdditive,
                        fifthAdditive
                )))
        )));
        assertEquals(expected, parser.parseExpression(input));
    }


    @Test
    void regularExpressionToString() {
        String expected = "(2*x+7)*(3*x^2-x+1)";

        var firstAdditive = new Additive(2, 1);
        var secondAdditive = new Additive(7, 0);

        var thirdAdditive = new Additive(3, 2);
        var fourthAdditive = new Additive(-1, 1);
        var fifthAdditive = new Additive(1, 0);

        Expression input = new Expression(new ArrayList<>(List.of(
                new Polynomial(new ArrayList<>(List.of(
                        firstAdditive,
                        secondAdditive
                ))),
                new Polynomial(new ArrayList<>(List.of(
                        thirdAdditive,
                        fourthAdditive,
                        fifthAdditive
                )))
        )));
        assertEquals(expected, parser.exressionToString(input));
    }


    @Test
    void parsePolynomialBasic() {
        String input = "-5*x^3+5-3*x";

        var firstAdditive = new Additive(-5, 3);
        var secondAdditive = new Additive(5, 0);
        var thirdAdditive = new Additive(-3, 1);


        Polynomial expected = new Polynomial(new ArrayList<>(List.of(
                firstAdditive,
                secondAdditive,
                thirdAdditive
        )));
        assertEquals(expected, parser.toMultiplier(input));
    }

    @Test
    void parsePolynomialAllCasesInOne() {
        String input = "3*x^2-5*x+3-x^3";

        var firstAdditive = new Additive(3, 2);
        var secondAdditive = new Additive(-5, 1);

        var thirdAdditive = new Additive(3, 0);
        var fourthAdditive = new Additive(-1, 3);


        Polynomial expected = new Polynomial(new ArrayList<>(List.of(
                firstAdditive,
                secondAdditive,
                thirdAdditive,
                fourthAdditive
        )));
        assertEquals(expected, parser.toMultiplier(input));
    }

}
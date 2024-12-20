package test.assignment.polynomial.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.assignment.polynomial.service.domain.Expression;
import test.assignment.polynomial.service.domain.Expression.Polynomial;
import test.assignment.polynomial.service.domain.Expression.Polynomial.Additive;
import test.assignment.polynomial.service.impl.ExpressionParserImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ExpressionParserImpl.class})
class ExpressionParserTest {

    @Autowired
    private ExpressionParserImpl parser;

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
        assertEquals(expected, parser.expressionToString(input));
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
        assertEquals(expected, parser.expressionToString(input));
    }

    @Test
    void simplifiedToString() {
        String expected = "6*x^3+19*x^2-5*x+7";

        var firstAdditive = new Additive(7, 0);
        var secondAdditive = new Additive(-5, 1);
        var thirdAdditive = new Additive(19, 2);
        var fourthAdditive = new Additive(6, 3);

        Expression input = new Expression(new ArrayList<>(List.of(
                new Polynomial(new ArrayList<>(List.of(
                        firstAdditive,
                        secondAdditive,
                        thirdAdditive,
                        fourthAdditive
                )))
        )));
        assertEquals(expected, parser.expressionToString(input));
    }

    @Test
    void differentCasesToString() {
        String expected = "3*x^2-3*x^2+x-x+2*x-2*x+2+4-3+1-1";

        var firstAdditive = new Additive(2, 0);
        var secondAdditive = new Additive(4, 0);
        var thirdAdditive = new Additive(-3, 0);
        var fourthAdditive = new Additive(1, 0);
        var fifthAdditive = new Additive(-1, 0);
        var sixthAdditive = new Additive(1, 1);
        var seventhAdditive = new Additive(-1, 1);
        var eighthAdditive = new Additive(2, 1);
        var ninethAdditive = new Additive(-2, 1);
        var tenthAdditive = new Additive(3, 2);
        var eleventhAdditive = new Additive(-3, 2);

        Expression input = new Expression(new ArrayList<>(List.of(
                new Polynomial(new ArrayList<>(List.of(
                        firstAdditive,
                        secondAdditive,
                        thirdAdditive,
                        fourthAdditive,
                        fifthAdditive,
                        sixthAdditive,
                        seventhAdditive,
                        eighthAdditive,
                        ninethAdditive,
                        tenthAdditive,
                        eleventhAdditive
                )))
        )));
        assertEquals(expected, parser.expressionToString(input));
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
    void parsePolynomialDifferentCases() {
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

    @Test
    void parsePolynomial1() {
        String input = "3*x^2";

        var firstAdditive = new Additive(3, 2);

        Polynomial expected = new Polynomial(new ArrayList<>(List.of(
                firstAdditive
        )));
        assertEquals(expected, parser.toMultiplier(input));
    }

    @Test
    void parsePolynomial2() {
        String input = "x^2";

        var firstAdditive = new Additive(1, 2);

        Polynomial expected = new Polynomial(new ArrayList<>(List.of(
                firstAdditive
        )));
        assertEquals(expected, parser.toMultiplier(input));
    }

    @Test
    void parsePolynomial3() {
        String input = "2";

        var firstAdditive = new Additive(2, 0);

        Polynomial expected = new Polynomial(new ArrayList<>(List.of(
                firstAdditive
        )));
        assertEquals(expected, parser.toMultiplier(input));
    }

    @Test
    void parsePolynomialAllCasesInOne() {
        String input = "7-5*x+1945*x^2+6*x^32";

        var firstAdditive = new Additive(7, 0);
        var secondAdditive = new Additive(-5, 1);

        var thirdAdditive = new Additive(1945, 2);
        var fourthAdditive = new Additive(6, 32);


        Expression expected = new Expression(List.of(
                new Polynomial(new ArrayList<>(List.of(
                        firstAdditive,
                        secondAdditive,
                        thirdAdditive,
                        fourthAdditive
                )))
        ));
        assertEquals(expected, parser.parseExpression(input));
    }

}
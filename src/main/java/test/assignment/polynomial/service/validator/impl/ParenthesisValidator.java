package test.assignment.polynomial.service.validator.impl;

import test.assignment.polynomial.exceptions.MalformedExpressionException;
import test.assignment.polynomial.service.validator.BaseValidator;
import test.assignment.polynomial.service.validator.PolynomialValidator;

import java.util.EmptyStackException;
import java.util.Stack;

import static java.lang.Math.min;

public class ParenthesisValidator extends BaseValidator {

    private static final char PLUS = '+';
    private static final char MINUS = '-';
    private static final char PRODUCT = '*';
    private static final char OPENING_BRACKET = '(';
    private static final char CLOSING_BRACKET = ')';

    private final PolynomialValidator validator;

    public ParenthesisValidator(PolynomialValidator validator) {
        this.validator = validator;
    }

    @Override
    public void validateExpressionString(String expression) {
        validator.validateExpressionString(expression);

        checkParenthesesNumber(expression);
        checkOpeningParenthesis(expression);
        checkClosingParenthesis(expression);
        checkX(expression);
    }

    private void checkParenthesesNumber(String expression) {
        Stack<Character> buffer = new Stack<>();
        try {
            for (char s : expression.toCharArray()) {
                if (s == OPENING_BRACKET)
                    buffer.push(s);
                else if (s == CLOSING_BRACKET)
                    buffer.pop();
            }
        } catch (EmptyStackException e) {
            throw new MalformedExpressionException("Each opening bracket must have a closing one!");
        }
        if (!buffer.isEmpty())
            throw new MalformedExpressionException("Each opening bracket must have a closing one!");
    }

    private void checkOpeningParenthesis(String expression) {//substitute to method above
        for (int i = 1; i < expression.length(); i++) {
            if (expression.charAt(i) == OPENING_BRACKET) {
                char expectedSign = expression.charAt(i - 1);
                if (!isSign(expectedSign))
                    throw new MalformedExpressionException();
            }
        }
    }

    private void checkClosingParenthesis(String expression) {
        for (int i = 1; i < expression.length() - 1; i++) {
            if (expression.charAt(i) == CLOSING_BRACKET) {
                char expectedSign = expression.charAt(i + 1);
                boolean hasSign = isSign(expectedSign);

                String expectedPower = powerSubstring(i + 1, expression);
                boolean hasPower = expectedPower.matches("\\^\\d+");

                if (!hasPower && !hasSign)
                    throw new MalformedExpressionException("Malformed expression!");

            }
        }
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

    private boolean isSign(char symbol) {
        return (symbol == PLUS || symbol == MINUS || symbol == PRODUCT);
    }

    private String powerSubstring(int from, String expression) {
        int plusIdx = expression.indexOf(PLUS, from);
        int minusIdx = expression.indexOf(MINUS, from);
        int productIdx = expression.indexOf(PRODUCT, from);

        int signIdx = min(min(plusIdx, minusIdx), productIdx);
        return expression.substring(from, signIdx);
    }
}

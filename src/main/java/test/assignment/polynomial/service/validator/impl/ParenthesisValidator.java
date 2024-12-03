package test.assignment.polynomial.service.validator.impl;

import test.assignment.polynomial.exceptions.MalformedExpressionException;
import test.assignment.polynomial.service.validator.BaseValidator;
import test.assignment.polynomial.service.validator.PolynomialValidator;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParenthesisValidator extends BaseValidator {

    private static final char PLUS = '+';
    private static final char MINUS = '-';
    private static final char PRODUCT = '*';

    private final PolynomialValidator validator;

    public ParenthesisValidator(PolynomialValidator validator) {
        this.validator = validator;
    }

    @Override
    public void validateExpressionString(String expression) {
        validator.validateExpressionString(expression);
        checkParenthesesNumber(expression);
    }

    private void checkParenthesesNumber(String expression) {
        Stack<Character> buffer = new Stack<>();
        try {
            char[] chars = expression.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char character = chars[i];
                if (character == OPENING_BRACKET) {
                    buffer.push(character);
                    checkOpeningBracket(expression, i);
                } else if (character == CLOSING_BRACKET) {
                    buffer.pop();
                    checkClosingBracket(expression, i);
                }
            }
        } catch (EmptyStackException e) {
            throw new MalformedExpressionException("Each opening bracket must have a closing one!");
        }
        if (!buffer.isEmpty())
            throw new MalformedExpressionException("Each opening bracket must have a closing one!");
    }

    private void checkOpeningBracket(String expression, int i) {
        if (i > 1) {
            char expectedSign = expression.charAt(i - 1);
            if (!isSign(expectedSign))
                throw new MalformedExpressionException();
        }
    }

    private void checkClosingBracket(String expression, int i) {
        if (i < expression.length() - 1) {
            char expectedSign = expression.charAt(i + 1);
            boolean hasSign = isSign(expectedSign);

            if (!hasSign) {
                int powerIdx = powerSubstring(i + 1, expression);
                if (powerIdx == i + 1)
                    throw new MalformedExpressionException("Malformed expression!");
            }
        }
    }

    private boolean isSign(char symbol) {
        return (symbol == PLUS || symbol == MINUS || symbol == PRODUCT);
    }

    private int powerSubstring(int from, String expression) {
        Matcher match = Pattern.compile("(?<power>\\^\\d+)").matcher(expression.substring(from));
        if (match.matches())
            return from + match.group("power").length();

        return from;
    }
}

package test.assignment.polynomial.service;

import org.springframework.stereotype.Service;

import java.util.EmptyStackException;
import java.util.Stack;

import static java.lang.Math.min;



//todo: Utilize Decorator design pattern when the service is ready
@Service
public class PolynomialValidator implements Validator {


    private static final char PLUS = '+';
    private static final char MINUS = '-';
    private static final char PRODUCT = '*';

    @Override
    public void validateExpressionString(String expression) {
        if (expression.isBlank())
            throw new EmptyExpressionException();

        if (!expression.matches("[\\d-+*^(x)]+"))
            throw new MalformedExpressionException();

        checkParenthesesNumber(expression);

        checkOpeningParenthesis(expression);
        checkClosingParenthesis(expression);
        checkX(expression);
    }


    private void checkParenthesesNumber(String expression) {
        Stack<Character> buffer = new Stack<>();
        try {
            for (char s : expression.toCharArray()) {
                if (s == '(')
                    buffer.push(s);
                else if (s == ')')
                    buffer.pop();
            }
        } catch (EmptyStackException e) {
            throw new MalformedExpressionException();
        }
        if (!buffer.isEmpty())
            throw new MalformedExpressionException();
    }

    private void checkOpeningParenthesis(String expression) {//substitute to method above
        for (int i = 1; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                char expectedSign = expression.charAt(i - 1);
                if (!isSign(expectedSign))
                    throw new MalformedExpressionException();
            }
        }
    }

    private void checkClosingParenthesis(String expression) {
        for (int i = 1; i < expression.length() - 1; i++) {
            if (expression.charAt(i) == ')') {
                char expectedSign = expression.charAt(i + 1);
                boolean hasSign = isSign(expectedSign);

                String expectedPower = powerSubstring(i + 1, expression);
                boolean hasPower = expectedPower.matches("\\^\\d+");

                if (!hasPower && !hasSign)
                    throw new MalformedExpressionException();

            }
        }
    }

    private void checkX(String expression) {
        for (int i = 1; i < expression.length() - 1; i++) {
            if (expression.charAt(i) == 'x') {
                String leftSubstring = expression.substring(0, i);
                if (expression.charAt(i - 1) != '(' && !leftSubstring.matches(".*\\d+[+\\-*]$"))
                    throw new MalformedExpressionException();

                String rightSubstring = expression.substring(i + 1);
                if (expression.charAt(i + 1) != ')' && !rightSubstring.matches("^[+\\-*]\\d+.*") && !rightSubstring.matches("^\\^\\d+.*"))
                    throw new MalformedExpressionException();

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

    @Override
    public void validateVariableValue(String x) {
        if (x == null || x.isBlank())
            throw new EmptyExpressionException();

        try {
            Double.parseDouble(x);
        } catch (NumberFormatException e){
            throw new NotDecimalVariableValueException();
        }
    }
}

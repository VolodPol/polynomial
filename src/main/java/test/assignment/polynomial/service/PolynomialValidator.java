package test.assignment.polynomial.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;
//\d?x(\^\d)?

@Service
public class PolynomialValidator implements Validator {


    @Override
    public void validateExpressionString(String expression) {
        if (expression.isBlank())
            throw new EmptyExpressionException();

        if (!expression.matches("[\\d-+*^(x)]+"))
            throw new MalformedExpressionException();

        int num = checkParenthesesNumber(expression);
        String noParentheses = expression.replaceAll("(\\([-+]?)|(\\))", "");// del all (+ or )
        boolean suitable = Arrays.stream(noParentheses.split("[-+]"))
                .allMatch(additive -> additive.matches("(\\d\\*\\d)*x?(\\^\\d)?"));//todo fix first part, ex.: 7*3*x...

        if (!suitable) {
            throw new MalformedExpressionException();
        }
    }


    private int checkParenthesesNumber(String expression) {
        int numOfGroups = 0;
        Stack<Character> buffer = new Stack<>();
        try {
            for (char s : expression.toCharArray()) {
                if (s == '(')
                    buffer.push(s);
                else if (s == ')') {
                    numOfGroups++;
                    buffer.pop();
                }
            }
        } catch (EmptyStackException e) {
            throw new MalformedExpressionException();
        }
        if (!buffer.isEmpty())
            throw new MalformedExpressionException();
        return numOfGroups;
    }

    @Override
    public void validateVariableValue(String x) {
        if (x.isBlank())
            throw new EmptyExpressionException();

        try {
            Double.parseDouble(x);
        } catch (NumberFormatException e){
            throw new NotDecimalVariableValueException();
        }
    }
}

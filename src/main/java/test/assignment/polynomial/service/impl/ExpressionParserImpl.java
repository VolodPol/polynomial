package test.assignment.polynomial.service.impl;

import org.springframework.stereotype.Component;
import test.assignment.polynomial.service.ExpressionParser;
import test.assignment.polynomial.service.domain.Expression;
import test.assignment.polynomial.service.domain.Expression.Polynomial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Component
public class ExpressionParserImpl implements ExpressionParser {
    @Override
    public Expression parseExpression(String representation) {
        Expression expression = new Expression();
        String[] split = representation.split("[)]\\*[(]");
        List<String> polynomialStrings = split.length == 1 ?
                Arrays.stream(split).toList()
                : Arrays.stream(split).map(this::removeBrackets)
                .toList();

        List<Polynomial> multipliers = new ArrayList<>();
        if (polynomialStrings.size() > 1)
            polynomialStrings.forEach(string -> multipliers.add(this.toMultiplier(string)));
        else {
            String element = polynomialStrings.getFirst();
            Matcher powerExtractor = Pattern.compile("\\([^()]+\\)\\^(?<pow>\\d+)")
                    .matcher(element);
            int number = 1;
            if (powerExtractor.matches()) {
                number = Integer.parseInt(powerExtractor.group("pow"));
                element = element.substring(1, element.length() - 3);
            }

            for (int i = 0; i < number; i++)
                multipliers.add(this.toMultiplier(element));
        }

        multipliers.forEach(expression::addMultiplier);
        return expression;
    }

    public Polynomial toMultiplier(String string) {
        List<Polynomial.Additive> additives = new ArrayList<>();
        boolean isMinus = false;
        String[] units = divide(string);

        int index = 0;
        while(index < units.length) {
            int coefficient = 1, power = 1;
            if (units[index].equals("-")) {
                isMinus = true;
                index++;
            } else if (units[index].equals("+")){
                isMinus = false;
                index++;
            }

            try {
                if (isNumber(units, index)) {
                    coefficient = Integer.parseInt(units[index++]);

                    if (index == units.length || !units[index].equals("*"))
                        power = 0;
                    else
                        index++;
                }
                if (units[index].equals("x") && units[++index].equals("^") && isNumber(units, ++index))
                    power = Integer.parseInt(units[index++]);
            } catch (ArrayIndexOutOfBoundsException _){}

            if (isMinus) coefficient *= -1;
            additives.add(new Polynomial.Additive(coefficient, power));
        }
        return new Polynomial(additives);
    }

    private String[] divide(String string) {
        List<String> split = new ArrayList<>(List.of(string.split("")));
        int idx = 0;

        while (idx < split.size() - 1) {
            int first = idx;
            if (Character.isDigit(split.get(idx).charAt(0)))
                while (idx < split.size() - 1 && Character.isDigit(split.get(idx + 1).charAt(0)))
                    idx++;

            if (first != idx) {
                StringBuilder n = new StringBuilder();
                split.subList(first, idx + 1).forEach(n::append);
                split.set(first, n.toString());
                split.subList(first + 1, idx + 1).clear();
            }
            idx++;
        }
        return split.toArray(new String[0]);
    }

    private boolean isNumber(String[] symbols, int index) {
        return symbols[index].matches("\\d+");
    }

    private String removeBrackets(String s) {
        if (s.startsWith("("))
            return s.substring(1);
        else if (s.endsWith(")"))
            return s.substring(0, s.length() - 1);

        return s;
    }

    @Override
    public String expressionToString(Expression expression) {
        List<String> multipliers = expression.getMultipliers().stream()
                .map(multiplier -> "(%s)".formatted(
                        multiplier.getAdditives().stream()
                                .map(this::mapAdditive)
                                .collect(Collectors.joining("+"))
                                .replaceAll("[+]-", "-")
                                .replaceAll("[+][+]", "+")
                ))
                .toList();

        String result = String.join("*", multipliers);
        return multipliers.size() > 1
                ? result
                : result.substring(1, result.length() - 1);
    }

    private String mapAdditive(Polynomial.Additive additive) {
        StringBuilder element = new StringBuilder();
        final int c = additive.getCoefficient();
        final int power = additive.getExponent();
        boolean nonZeroPower = power != 0;

        if (nonZeroPower) {
            element.append("x");
            if (power != 1)
                element.append("^%d".formatted(power));
        }

        if (!nonZeroPower)
            element.insert(0, c);
        else if (c == 1)
            element.insert(0, "+");
        else if (c == -1)
            element.insert(0, "-");
        else
            element.insert(0, "%d*".formatted(c));
        return element.toString();
    }
}

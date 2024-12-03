package test.assignment.polynomial.service.impl;

import org.springframework.stereotype.Component;
import test.assignment.polynomial.service.ExpressionParser;
import test.assignment.polynomial.service.domain.Expression;
import test.assignment.polynomial.service.domain.Expression.Polynomial;
import test.assignment.polynomial.service.domain.Expression.Polynomial.Additive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Component
public class ExpressionParserImpl implements ExpressionParser {
    private static final String PRODUCT = "*";
    private static final String PLUS = "+";
    private static final String MINUS = "-";
    private static final String X = "x";
    private static final String POWER_OPERATOR = "^";
    private static final String EMPTY_STRING = "";

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
        List<Additive> additives = new ArrayList<>();
        boolean isMinus = false;
        String[] units = divide(string);

        int index = 0;
        while(index < units.length) {
            int coefficient = 1, power = 1;
            if (units[index].equals(MINUS)) {
                isMinus = true;
                index++;
            } else if (units[index].equals(PLUS)){
                isMinus = false;
                index++;
            }

            try {
                if (isNumber(units, index)) {
                    coefficient = Integer.parseInt(units[index++]);

                    if (index == units.length || !units[index].equals(PRODUCT))
                        power = 0;
                    else
                        index++;
                }
                if (units[index].equals(X) && units[++index].equals(POWER_OPERATOR) && isNumber(units, ++index))
                    power = Integer.parseInt(units[index++]);
            } catch (ArrayIndexOutOfBoundsException _){}

            if (isMinus) coefficient *= -1;
            additives.add(new Additive(coefficient, power));
        }
        return new Polynomial(additives);
    }

    private String[] divide(String string) {
        List<String> split = new ArrayList<>(List.of(string.split(EMPTY_STRING)));
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
                .map(multiplier -> {
                    List<Additive> sorted = multiplier.getAdditives().stream()
                            .sorted((o1, o2) -> Integer.compare(o2.getExponent(), o1.getExponent()))
                            .toList();
                    String first = sorted.isEmpty() ? EMPTY_STRING : this.additiveToString(sorted.getFirst());
                    return "(%s)".formatted(
                            first.concat(
                                    sorted.stream().skip(1)
                                            .map(mapAdditive())
                                            .collect(Collectors.joining())
                            )
                    );
                })
                .toList();

        String result = String.join(PRODUCT, multipliers);
        return multipliers.size() > 1
                ? result
                : result.substring(1, result.length() - 1);
    }

    private Function<Additive, String> mapAdditive() {
        return additive -> {
            String text = this.additiveToString(additive);
            if (!text.startsWith(PLUS) && !text.startsWith(MINUS))
                return PLUS.concat(text);

            return text;
        };
    }

    private String additiveToString(Additive additive) {
        StringBuilder element = new StringBuilder();
        final int c = additive.getCoefficient();
        final int power = additive.getExponent();
        boolean nonZeroPower = power != 0;

        if (nonZeroPower) {
            element.append(X);
            if (power != 1)
                element.append("%s%d".formatted(POWER_OPERATOR, power));
        }

        if (!nonZeroPower)
            element.insert(0, c);
        else if (c == 1)
            element.insert(0, PLUS);
        else if (c == -1)
            element.insert(0, MINUS);
        else
            element.insert(0, "%d%s".formatted(c, PRODUCT));
        return element.toString();
    }
}
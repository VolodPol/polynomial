package test.assignment.polynomial.service.impl;

import org.springframework.stereotype.Service;
import test.assignment.polynomial.service.ExpressionParser;
import test.assignment.polynomial.service.domain.Expression;
import test.assignment.polynomial.service.domain.Expression.Polynomial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ExpressionParserImpl implements ExpressionParser {
    @Override
    public Expression parseExpression(String representation) {
        Expression expression = new Expression();
        List<String> polynomialStrings = Arrays.stream(representation.split("[)]\\*[(]"))
                .map(this::removeBrackets)
                .toList();

        List<Polynomial> multipliers = new ArrayList<>();

        if (polynomialStrings.size() > 1) {
            polynomialStrings.forEach(string -> multipliers.add(this.toMultiplier(string)));
        } else {
            //case with:     (...)^n
            Matcher powerExtractor = Pattern.compile("\\([^()]+\\)\\^(?<pow>\\d+)")
                    .matcher(polynomialStrings.getFirst());
            int number = powerExtractor.matches() ? Integer.parseInt(powerExtractor.group("pow")) : 1;

            for (int i = 0; i < number; i++) {
                multipliers.add(this.toMultiplier(polynomialStrings.getFirst()));
            }
        }

        multipliers.forEach(expression::addMultiplier);
        return expression;
    }

    public Polynomial toMultiplier(String string) {
        List<Polynomial.Additive> additives = new ArrayList<>();
        boolean isMinus = false;
        String[] symbols = string.split("");

        int index = 0;
        while(index < symbols.length) {
            int coefficient = 1, power = 1;
            if (symbols[index].equals("-")) {//todo: refactor(move after exception handling)
                isMinus = true;
                index++;
            } else if (symbols[index].equals("+")){
                isMinus = false;
                index++;
            }

            try {
                if (isNumber(symbols, index)) {
                    coefficient = Integer.parseInt(symbols[index++]);

                    if (index == symbols.length || !symbols[index].equals("*"))
                        power = 0;
                    else
                        index++;
                }
                if (symbols[index].equals("x") && symbols[++index].equals("^") && isNumber(symbols, ++index))
                    power = Integer.parseInt(symbols[index++]);
            } catch (ArrayIndexOutOfBoundsException _){}

            if (isMinus) coefficient *= -1;
            additives.add(new Polynomial.Additive(coefficient, power));
        }
        return new Polynomial(additives);
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
    public String exressionToString(Expression expression) {
        return "";
    }
}

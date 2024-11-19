package test.assignment.polynomial.service;

import org.springframework.stereotype.Service;
import test.assignment.polynomial.service.domain.Expression;
import test.assignment.polynomial.service.domain.Expression.Polynomial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ExpressionParser implements Parser {
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

    private Polynomial toMultiplier(String string) {

        return null;
    }


//    private Polynomial toMultiplier(String string) {
//        int tracker = 0;
//        Stack<Integer> coefficients = new Stack<>();
//
//        String[] symbols = string.split("");
//        while (tracker < string.length()) {
//            if (symbols[tracker].matches("\\d+")) {
//                if (coefficients.isEmpty()) {
//                    coefficients.push(Integer.parseInt(symbols[tracker]));
//                } else {
//
//                }
//            }
//
//        }
//
//
//        return null;
//    }

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

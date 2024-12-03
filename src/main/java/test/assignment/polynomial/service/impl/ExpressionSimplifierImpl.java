package test.assignment.polynomial.service.impl;

import org.springframework.stereotype.Component;
import test.assignment.polynomial.service.ExpressionSimplifier;
import test.assignment.polynomial.service.domain.Expression;
import test.assignment.polynomial.service.domain.Expression.Polynomial;
import test.assignment.polynomial.service.domain.Expression.Polynomial.Additive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

@Component
public class ExpressionSimplifierImpl extends ExpressionSimplifier {

    @Override
    protected void simplifyMultipliers(Expression expression) {
        expression.getMultipliers()
                .forEach(polynomial -> {
                    Map<Integer, Optional<Additive>> map = polynomial.getAdditives().stream()
                            .collect(additiveSimplifier());
                    List<Additive> simplified = map.values().stream()
                            .<Additive>mapMulti(Optional::ifPresent)
                            .collect(Collectors.toList());
                    polynomial.setAdditives(simplified);
                });
    }

    private Collector<Additive, ?, Map<Integer, Optional<Additive>>> additiveSimplifier() {
        return groupingBy(
                Additive::getExponent,
                reducing((a, b) -> new Additive(
                        a.getCoefficient() + b.getCoefficient(),
                        a.getExponent()
                )));
    }

    @Override
    protected void applyProduct(Expression expression) {
        Polynomial one = new Polynomial();
        one.addAdditive(new Additive(1, 0));
        expression.setMultipliers(
                new ArrayList<>(List.of(
                        expression.getMultipliers().stream()
                                .reduce(one, productOperator())
                ))
        );
    }

    private BinaryOperator<Polynomial> productOperator() {
        return (p1, p2) -> {
            List<Additive> p1Additives = p1.getAdditives();
            List<Additive> p2Additives = p2.getAdditives();

            List<Additive> simplifiedAdditives = new ArrayList<>();
            for (Additive p1Additive : p1Additives) {
                for (Additive p2Additive : p2Additives) {
                    simplifiedAdditives.add(
                            new Additive(
                                    p1Additive.getCoefficient() * p2Additive.getCoefficient(),
                                    p1Additive.getExponent() + p2Additive.getExponent()
                            )
                    );
                }
            }

            return new Polynomial(simplifiedAdditives);
        };
    }
}

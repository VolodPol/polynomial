package test.assignment.polynomial.service.impl;

import org.springframework.stereotype.Component;
import test.assignment.polynomial.service.ExpressionSimplifier;
import test.assignment.polynomial.service.domain.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

@Component
public class ExpressionSimplifierImpl extends ExpressionSimplifier {

    @Override
    protected void simplifyMultipliers(Expression expression) {
        expression.getMultipliers()
                .forEach(polynomial -> {
                    Map<Integer, Optional<Expression.Polynomial.Additive>> map = polynomial.getAdditives().stream()
                            .collect(groupingBy(
                                    Expression.Polynomial.Additive::getExponent,
                                    reducing((a, b) -> new Expression.Polynomial.Additive(
                                            a.getCoefficient() + b.getCoefficient(),
                                            a.getExponent()
                                    )))
                            );
                    List<Expression.Polynomial.Additive> simplified = map.values().stream()
                            .<Expression.Polynomial.Additive>mapMulti(Optional::ifPresent)
                            .collect(Collectors.toList());
                    polynomial.setAdditives(simplified);
                });
    }

    @Override
    protected void applyProduct(Expression expression) {
        Expression.Polynomial one = new Expression.Polynomial();
        one.addAdditive(new Expression.Polynomial.Additive(1, 0));
        expression.setMultipliers(
                new ArrayList<>(List.of(
                        expression.getMultipliers().stream()
                                .reduce(one, (p1, p2) -> {
                                    List<Expression.Polynomial.Additive> p1Additives = p1.getAdditives();
                                    List<Expression.Polynomial.Additive> p2Additives = p2.getAdditives();

                                    List<Expression.Polynomial.Additive> simplifiedAdditives = new ArrayList<>();
                                    for (Expression.Polynomial.Additive p1Additive : p1Additives) {
                                        for (Expression.Polynomial.Additive p2Additive : p2Additives) {
                                            simplifiedAdditives.add(
                                                    new Expression.Polynomial.Additive(
                                                            p1Additive.getCoefficient() * p2Additive.getCoefficient(),
                                                            p1Additive.getExponent() + p2Additive.getExponent()
                                                    )
                                            );
                                        }
                                    }

                                    return new Expression.Polynomial(simplifiedAdditives);
                                })
                )
                )
        );
    }
}

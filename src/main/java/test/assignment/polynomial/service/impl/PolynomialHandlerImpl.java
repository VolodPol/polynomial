package test.assignment.polynomial.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import test.assignment.polynomial.entity.Evaluation;
import test.assignment.polynomial.entity.RawExpression;
import test.assignment.polynomial.entity.SimplifiedExpression;
import test.assignment.polynomial.exceptions.NotExistingSimplifiedExpressionException;
import test.assignment.polynomial.repository.EvaluationRepository;
import test.assignment.polynomial.repository.RawExpressionRepository;
import test.assignment.polynomial.repository.SimplifiedExpressionRepository;
import test.assignment.polynomial.service.ExpressionParser;
import test.assignment.polynomial.service.PolynomialHandler;
import test.assignment.polynomial.service.PolynomialValidator;
import test.assignment.polynomial.service.domain.Expression;
import test.assignment.polynomial.service.domain.Expression.Polynomial;
import test.assignment.polynomial.service.domain.Expression.Polynomial.Additive;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;


@Service
@AllArgsConstructor
public class PolynomialHandlerImpl implements PolynomialHandler {
    private final PolynomialValidator validator;
    private final ExpressionParser parser;
    private final SimplifiedExpressionRepository simplifiedRepository;
    private final RawExpressionRepository rawRepository;
    private final EvaluationRepository evaluationRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public SimplifiedExpression simplify(String raw) {
        validator.validateExpressionString(raw);

        SimplifiedExpression simplifiedExpression = simplifiedRepository.findByRawExpression(raw);
        if (simplifiedExpression != null)
            return simplifiedExpression;

        Expression expression = parser.parseExpression(raw);
        simplifyExpression(expression);
        String simplified = parser.expressionToString(expression);

        RawExpression rawEntity = RawExpression.builder().expression(raw).build();
        SimplifiedExpression simplifiedEntity = new SimplifiedExpression();
        simplifiedEntity.setExpression(simplified);
        simplifiedRepository.save(simplifiedEntity);
        simplifiedEntity.addRawExpression(rawEntity);
        rawRepository.save(rawEntity);

        return simplifiedEntity;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public double evaluate(String simplified, double value) {
        validator.validateExpressionString(simplified);

        SimplifiedExpression simplifiedExpression = simplifiedRepository.findSimplifiedExpressionByExpression(simplified);
        if (simplifiedExpression == null){
            throw new NotExistingSimplifiedExpressionException("Not existing simplified expression!");
        }

        Evaluation foundEvaluation = evaluationRepository.findBySimplifiedExpression(simplified);
        if (foundEvaluation != null) {
            return foundEvaluation.getResult();
        }

        Expression expression = parser.parseExpression(simplified);
        double result = evaluateExpression(expression, value);
        Evaluation newEvaluation = Evaluation.builder()
                .result(result)
                .build();
        evaluationRepository.save(newEvaluation);


        simplifiedExpression.addEvaluation(newEvaluation);
        return result;
    }

    private void simplifyExpression(Expression expression) {
        simplifyMultipliers(expression);
        applyProduct(expression);
        simplifyMultipliers(expression);
    }

    private void simplifyMultipliers(Expression expression) {
        expression.getMultipliers()
                .forEach(polynomial -> {
                    Map<Integer, Optional<Additive>> map = polynomial.getAdditives().stream()
                            .collect(groupingBy(
                                    Additive::getExponent,
                                    reducing((a, b) -> new Additive(
                                            a.getCoefficient() + b.getCoefficient(),
                                            a.getExponent()
                                    )))
                            );
                    List<Additive> simplified = map.values().stream()
                            .<Additive>mapMulti(Optional::ifPresent)
                            .collect(Collectors.toList());
                    polynomial.setAdditives(simplified);
                });
    }

    private void applyProduct(Expression expression) {
        Polynomial one = new Polynomial();
        one.addAdditive(new Additive(1, 0));
        expression.setMultipliers(
                new ArrayList<>(List.of(
                        expression.getMultipliers().stream()
                                .reduce(one, (p1, p2) -> {
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
                                })
                        )
                )
        );
    }

    //restriction: only 1 multiplier
    private double evaluateExpression(Expression expression, double value) {
        return expression.getMultipliers().stream()
                .flatMap(p -> p.getAdditives().stream())
                .mapToDouble(a -> a.getCoefficient() * Math.pow(value, a.getExponent()))
                .sum();
    }
}

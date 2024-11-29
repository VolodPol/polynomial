package test.assignment.polynomial.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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
import test.assignment.polynomial.service.ExpressionSimplifier;
import test.assignment.polynomial.service.PolynomialHandler;
import test.assignment.polynomial.service.PolynomialValidator;
import test.assignment.polynomial.service.domain.Expression;


@Service
@AllArgsConstructor
public class PolynomialHandlerImpl implements PolynomialHandler {
    private final PolynomialValidator validator;
    private final ExpressionParser parser;
    private final ExpressionSimplifier simplifier;

    private final SimplifiedExpressionRepository simplifiedRepository;
    private final RawExpressionRepository rawRepository;
    private final EvaluationRepository evaluationRepository;

    @Override
    @Cacheable("simplified")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public SimplifiedExpression simplify(String raw) {
        validator.validateExpressionString(raw);

        SimplifiedExpression simplifiedExpression = simplifiedRepository.findByRawExpression(raw);
        if (simplifiedExpression != null)
            return simplifiedExpression;

        Expression expression = parser.parseExpression(raw);
        simplifier.simplifyExpression(expression);
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
    @Cacheable("evaluated")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public double evaluate(String simplified, String value) {
        validator.validateExpressionString(simplified);
        double doubleValue = validator.validateVariableValue(value);

        SimplifiedExpression simplifiedExpression = simplifiedRepository.findSimplifiedExpressionByExpression(simplified);
        if (simplifiedExpression == null){
            throw new NotExistingSimplifiedExpressionException("Not existing simplified expression!");
        }

        Evaluation foundEvaluation = evaluationRepository.findBySimplifiedExpression(simplified);
        if (foundEvaluation != null) {
            return foundEvaluation.getResult();
        }

        Expression expression = parser.parseExpression(simplified);
        double result = evaluateExpression(expression, doubleValue);
        Evaluation newEvaluation = Evaluation.builder()
                .result(result)
                .build();
        evaluationRepository.save(newEvaluation);


        simplifiedExpression.addEvaluation(newEvaluation);
        return result;
    }

    //restriction: only 1 multiplier
    private double evaluateExpression(Expression expression, double value) {
        return expression.getMultipliers().stream()
                .flatMap(p -> p.getAdditives().stream())
                .mapToDouble(a -> a.getCoefficient() * Math.pow(value, a.getExponent()))
                .sum();
    }
}

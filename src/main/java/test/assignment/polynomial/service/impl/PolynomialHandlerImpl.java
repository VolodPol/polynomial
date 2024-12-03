package test.assignment.polynomial.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import test.assignment.polynomial.dto.ExpressionDto;
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
import test.assignment.polynomial.service.validator.PolynomialValidator;
import test.assignment.polynomial.service.domain.Expression;


@Service
@AllArgsConstructor
public class PolynomialHandlerImpl implements PolynomialHandler {
    private final PolynomialValidator polynomialValidator;
    private final ExpressionParser parser;
    private final ExpressionSimplifier simplifier;

    private final SimplifiedExpressionRepository simplifiedRepository;
    private final RawExpressionRepository rawRepository;
    private final EvaluationRepository evaluationRepository;

    @Override
    @Cacheable("simplified")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public SimplifiedExpression simplify(ExpressionDto raw) {
        String rawExpression = raw.expression();
        polynomialValidator.validateExpressionString(rawExpression);

        SimplifiedExpression simplifiedExpression = simplifiedRepository.findByRawExpression(rawExpression);
        if (simplifiedExpression != null)
            return simplifiedExpression;

        Expression expression = parser.parseExpression(rawExpression);
        simplifier.simplifyExpression(expression);
        String simplified = parser.expressionToString(expression);

        RawExpression rawEntity = RawExpression.builder().expression(rawExpression).build();
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
    public double evaluate(ExpressionDto simplified, String value) {
        String inputExpression = simplified.expression();
        polynomialValidator.validateExpressionString(inputExpression);
        double doubleValue = polynomialValidator.validateVariableValue(value);

        SimplifiedExpression simplifiedExpression = simplifiedRepository.findSimplifiedExpressionByExpression(inputExpression);
        if (simplifiedExpression == null)
            throw new NotExistingSimplifiedExpressionException("Not existing simplified expression!");

        Evaluation foundEvaluation = evaluationRepository.findBySimplifiedExpression(inputExpression);
        if (foundEvaluation != null)
            return foundEvaluation.getResult();

        Expression expression = parser.parseExpression(inputExpression);
        double result = evaluateExpression(expression, doubleValue);
        Evaluation newEvaluation = Evaluation.builder()
                .result(result)
                .build();
        evaluationRepository.save(newEvaluation);


        simplifiedExpression.addEvaluation(newEvaluation);
        return result;
    }

    private double evaluateExpression(Expression expression, double value) {
        if (expression.getMultipliers().size() > 1)
            throw new IllegalArgumentException("The expression must be simplified!");
        return expression.getMultipliers().stream()
                .flatMap(p -> p.getAdditives().stream())
                .mapToDouble(a -> a.getCoefficient() * Math.pow(value, a.getExponent()))
                .sum();
    }
}
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

//TODO: replace @Service with @Component for: validator and parser
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
    public String simplify(String raw) {
        validator.validateExpressionString(raw);

        SimplifiedExpression simplifiedExpression = simplifiedRepository.findByRawExpression(raw);
        if (simplifiedExpression != null)
            return simplifiedExpression.getExpression();

        Expression expression = parser.parseExpression(raw);
        simplifyExpression(expression);
        String simplified = parser.expressionToString(expression);

        RawExpression rawEntity = RawExpression.builder().expression(raw).build();
        SimplifiedExpression simplifiedEntity = SimplifiedExpression.builder().expression(simplified).build();
        simplifiedRepository.save(simplifiedEntity);
        rawRepository.save(rawEntity);
        simplifiedEntity.addRawExpression(rawEntity);

        return simplified;
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
        //TODO: implement logic
    }

    private double evaluateExpression(Expression expression, double value) {
        //TODO: implement logic
        return 0;
    }
}

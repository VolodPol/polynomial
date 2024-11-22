package test.assignment.polynomial.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import test.assignment.polynomial.entity.RawExpression;

import java.util.List;

@Repository
public interface RawExpressionRepository extends BaseSaveRepository<RawExpression, Long> {
    List<RawExpression> findByExpression(@NotNull String expression);
}

package test.assignment.polynomial.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import test.assignment.polynomial.entity.SimplifiedExpression;

@Repository
public interface SimplifiedExpressionRepository extends JpaRepository<SimplifiedExpression, Long>, BaseSaveRepository<SimplifiedExpression, Long> {
    @Query("""
select s from SimplifiedExpression s join s.parentRawExpressions parents where parents.expression = :rawExpression
""")
    SimplifiedExpression findByRawExpression(String rawExpression);

    SimplifiedExpression findSimplifiedExpressionByExpression(@NotNull String expression);
}

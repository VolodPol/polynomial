package test.assignment.polynomial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import test.assignment.polynomial.entity.Evaluation;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long>, BaseSaveRepository<Evaluation, Long> {
    @Query("""
select e from Evaluation e join e.simplified s where s.expression = :simplifiedExpression
""")
    Evaluation findBySimplifiedExpression(String simplifiedExpression);
}

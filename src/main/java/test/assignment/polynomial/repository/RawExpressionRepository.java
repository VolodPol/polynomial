package test.assignment.polynomial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.assignment.polynomial.entity.RawExpression;

public interface RawExpressionRepository extends JpaRepository<RawExpression, Long> {


}

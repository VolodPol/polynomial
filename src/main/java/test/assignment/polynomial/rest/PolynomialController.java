package test.assignment.polynomial.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import test.assignment.polynomial.entity.RawExpression;
import test.assignment.polynomial.repository.RawExpressionRepository;

import java.util.List;

@RestController("api/")
@AllArgsConstructor
public class PolynomialController {
    private final RawExpressionRepository repository;

    @GetMapping("/students")
    public List<RawExpression> findAll() {
        return null;
    }
}

package test.assignment.polynomial.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import test.assignment.polynomial.dto.EvaluatedDto;
import test.assignment.polynomial.dto.ExpressionDto;
import test.assignment.polynomial.entity.SimplifiedExpression;
import test.assignment.polynomial.service.PolynomialHandler;

import java.net.URI;


@RestController
@RequestMapping("api")
@AllArgsConstructor
public class PolynomialController {
    private final PolynomialHandler handler;

    @PostMapping("simplify")
    public ResponseEntity<ExpressionDto> simplifyExpression(@RequestBody ExpressionDto expression) {
        SimplifiedExpression saved = handler.simplify(expression);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(new ExpressionDto(saved.getExpression()));
    }

    @PostMapping("evaluate")
    public ResponseEntity<EvaluatedDto> evaluateSimplifiedExpression(@RequestBody ExpressionDto simplified,
                                                               @RequestParam("value") String value) {
        double evaluated = handler.evaluate(simplified, value);
        return ResponseEntity.ok(new EvaluatedDto(
                simplified.expression(), value, evaluated
        ));
    }
}

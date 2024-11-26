package test.assignment.polynomial.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import test.assignment.polynomial.entity.SimplifiedExpression;
import test.assignment.polynomial.service.PolynomialHandler;

import java.net.URI;


@RestController
@RequestMapping("api")
@AllArgsConstructor
public class PolynomialController {
    private final PolynomialHandler handler;

    @PostMapping(value = "simplify", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> simplifyExpression(@RequestBody String expression) {
        SimplifiedExpression saved = handler.simplify(expression);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(saved.getExpression());
    }

    @PostMapping(value = "evaluate", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Double> evaluateSimplifiedExpression(@RequestBody String simplified,
                                                               @RequestParam("value") double value) {
        double evaluated = handler.evaluate(simplified, value);
        return ResponseEntity.ok(evaluated);
    }
}

package test.assignment.polynomial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test.assignment.polynomial.service.validator.impl.VariableValidator;
import test.assignment.polynomial.service.validator.impl.ParenthesisValidator;
import test.assignment.polynomial.service.validator.PolynomialValidator;
import test.assignment.polynomial.service.validator.impl.ContentValidator;

@Configuration
public class ValidationConfiguration {

    @Bean
    public PolynomialValidator polynomialValidator() {
        return new VariableValidator(
                new ParenthesisValidator(
                        new ContentValidator()
                )
        );
    }
}
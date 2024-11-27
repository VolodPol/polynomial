package test.assignment.polynomial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PolynomialApplication {

    public static void main(String[] args) {
        SpringApplication.run(PolynomialApplication.class, args);
    }

}

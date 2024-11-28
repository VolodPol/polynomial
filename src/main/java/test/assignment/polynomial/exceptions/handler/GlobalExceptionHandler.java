package test.assignment.polynomial.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import test.assignment.polynomial.exceptions.EmptyExpressionException;
import test.assignment.polynomial.exceptions.MalformedExpressionException;
import test.assignment.polynomial.exceptions.NotDecimalVariableValueException;
import test.assignment.polynomial.exceptions.NotExistingSimplifiedExpressionException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {
            EmptyExpressionException.class,
            MalformedExpressionException.class,
            NotDecimalVariableValueException.class,
            NotExistingSimplifiedExpressionException.class
    })
    public ErrorResponse handleCommonExceptions(RuntimeException exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}

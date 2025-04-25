package co.istad.mobilebanking.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationException(){
        return "Error Validation Exception";
    }

}

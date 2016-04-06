package br.com.camiloporto.marmitex.microservice.profile.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by ur42 on 06/04/2016.
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    HashMap<String, Object> catchConstraintViolationExceptions(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        ArrayList<String> errors = new ArrayList<>(constraintViolations.size());

        for (ConstraintViolation<?> cv :constraintViolations) {
            errors.add(cv.getMessage());
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("errors", errors);
        return result;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    HashMap<String, Object> catchThrowable(Throwable e) {

        HashMap<String, Object> result = new HashMap<>();
        result.put("internalError", e.getMessage());
        return result;
    }
}

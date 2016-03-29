package br.com.camiloporto.marmitex.microservice.profile.service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ur42 on 29/03/2016.
 */
public class BusinessValidator<T> {

    private Validator validator;

    public BusinessValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(T toValidate, Class<?> ruleGroupsToApply) throws ConstraintViolationException {
        Set<ConstraintViolation<T>> violations = validator.validate(toValidate, ruleGroupsToApply);
        throwNewConstraintViolationExceptionIfViolations(new HashSet<ConstraintViolation<?>>(violations));
    }

    private void throwNewConstraintViolationExceptionIfViolations(Set<ConstraintViolation<?>> violations) {
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }
}

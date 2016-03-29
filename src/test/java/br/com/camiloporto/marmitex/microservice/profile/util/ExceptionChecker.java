package br.com.camiloporto.marmitex.microservice.profile.util;

import org.testng.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * Created by ur42 on 29/03/2016.
 */
public class ExceptionChecker {

    private ConstraintViolationException exception;

    public ExceptionChecker(ConstraintViolationException e) {

        exception = e;
    }

    public ExceptionChecker assertErrorCount(int expectedErrorCount) {
        Assert.assertEquals(
                exception.getConstraintViolations().size(),
                expectedErrorCount,
                "error count did not match expected count"
        );
        return this;
    }

    public ExceptionChecker assertTemplateMessagePresentAndI18Nized(String expectedTemplateMsg) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        boolean found = false;
        for (ConstraintViolation<?> cv: constraintViolations) {
            found = cv.getMessageTemplate().equals(expectedTemplateMsg);
            if(found) {
                assertTemplateMessageIsI18Nized(cv);
                break;
            }
        }
        Assert.assertTrue(found, "expected error message template '" + expectedTemplateMsg + "' not found");
        return this;
    }

    private void assertTemplateMessageIsI18Nized(
            ConstraintViolation<?> constraintViolation) {
        Assert.assertNotEquals(
                constraintViolation.getMessage(),
                constraintViolation.getMessageTemplate(),
                "template error message not i18Nized: " + constraintViolation.getMessageTemplate() + ". check ValidationMessages.properties in the root of classpath");
    }
}

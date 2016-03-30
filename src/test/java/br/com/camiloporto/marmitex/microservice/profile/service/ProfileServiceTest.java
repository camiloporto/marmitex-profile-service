package br.com.camiloporto.marmitex.microservice.profile.service;

import br.com.camiloporto.marmitex.microservice.profile.AbstractMarmitexProfileTest;
import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.repository.RDMBSProfileRepository;
import br.com.camiloporto.marmitex.microservice.profile.util.ExceptionChecker;
import lombok.Setter;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;

/**
 * Created by ur42 on 29/03/2016.
 */
public class ProfileServiceTest extends AbstractMarmitexProfileTest {

    public static final String VALID_LOGIN = "camiloporto@email.com";
    @Autowired @Setter
    private ProfileService profileService;

    @Autowired
    private RDMBSProfileRepository profileRepository;

    @BeforeMethod
    public void deleteData() {
        profileRepository.deleteAll();
    }

    @Test
    public void shouldCreateNewProfile() {
        Profile p = new Profile(VALID_LOGIN, "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile saved = profileService.save(p);
        Assert.assertNotNull(saved.getProfileId());
    }

    @Test
    public void loginMustBeUnique() {
        Profile p1 = new Profile(VALID_LOGIN, "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile p2 = new Profile(VALID_LOGIN, "sup3rs3cr3t", "Camilo Facanha", "1234-5674", "8th St.");
        profileService.save(p1);
        try {
            profileService.save(p2);
            Assert.fail("should raised exception. profile login must be unique");
        } catch (ConstraintViolationException e) {
            new ExceptionChecker(e)
                    .assertErrorCount(1)
                    .assertTemplateMessagePresentAndI18Nized(
                            "{br.com.camiloporto.marmitex.microservice.profile.LOGIN_UNIQUE}");
        }
    }

    @Test
    public void loginMustBeValidEmail() {
        Profile p1 = new Profile("invalidEmail", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        try {
            profileService.save(p1);
            Assert.fail("should raised exception. profile login must be a valid email address");
        } catch (ConstraintViolationException e) {
            new ExceptionChecker(e)
                    .assertErrorCount(1)
                    .assertTemplateMessagePresentAndI18Nized(
                            "{br.com.camiloporto.marmitex.microservice.profile.INVALID_LOGIN}");
        }
    }

    @Test
    public void loginIsRequired() {
        String nullLogin = null;
        String emptyLogin = "";
        Profile p1 = new Profile(nullLogin, "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile p2 = new Profile(emptyLogin, "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        try {
            profileService.save(p1);
            Assert.fail("should raised exception. profile login must not be null");
        } catch (ConstraintViolationException e) {
            new ExceptionChecker(e)
                    .assertErrorCount(1)
                    .assertTemplateMessagePresentAndI18Nized(
                            "{br.com.camiloporto.marmitex.microservice.profile.LOGIN_REQUIRED}");
        }

        try {
            profileService.save(p2);
            Assert.fail("should raised exception. profile login must not be empty");
        } catch (ConstraintViolationException e) {
            new ExceptionChecker(e)
                    .assertErrorCount(1)
                    .assertTemplateMessagePresentAndI18Nized(
                            "{br.com.camiloporto.marmitex.microservice.profile.LOGIN_REQUIRED}");
        }
    }

    @Test
    public void passwordIsRequired() {
        String nullPass = null;
        String emptyPass = "";
        Profile p1 = new Profile(VALID_LOGIN, nullPass, "Camilo Porto", "8888-8765", "5th St.");
        Profile p2 = new Profile(VALID_LOGIN, emptyPass, "Camilo Porto", "8888-8765", "5th St.");
        try {
            profileService.save(p1);
            Assert.fail("should raised exception. profile password must not be null");
        } catch (ConstraintViolationException e) {
            new ExceptionChecker(e)
                    .assertErrorCount(1)
                    .assertTemplateMessagePresentAndI18Nized(
                            "{br.com.camiloporto.marmitex.microservice.profile.PASSWORD_REQUIRED}");
        }

        try {
            profileService.save(p2);
            Assert.fail("should raised exception. profile password must not be empty");
        } catch (ConstraintViolationException e) {
            new ExceptionChecker(e)
                    .assertErrorCount(1)
                    .assertTemplateMessagePresentAndI18Nized(
                            "{br.com.camiloporto.marmitex.microservice.profile.PASSWORD_REQUIRED}");
        }
    }

    //FIXME add change password function and rescue password
}

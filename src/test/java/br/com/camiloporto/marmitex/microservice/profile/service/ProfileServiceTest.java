package br.com.camiloporto.marmitex.microservice.profile.service;

import br.com.camiloporto.marmitex.microservice.profile.AbstractMarmitexProfileTest;
import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.util.ExceptionChecker;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;

/**
 * Created by ur42 on 29/03/2016.
 */
public class ProfileServiceTest extends AbstractMarmitexProfileTest {

    public static final String VALID_LOGIN = "camiloporto@email.com";

    @Autowired @Setter
    private ProfileService profileService;

    private PasswordEncoder realPasswordEncoder;
    private PasswordEncoder fakePasswordEncoder;

    @AfterClass
    public void unmockPasswordEncoder() {
        ((ProfileServiceImpl)profileService).setPasswordEncoder(realPasswordEncoder);
    }

    @BeforeClass
    public void mockPasswordEncoder() {
        realPasswordEncoder = ((ProfileServiceImpl)profileService).getPasswordEncoder();
        fakePasswordEncoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return charSequence.toString().equals(s);
            }
        };
        ((ProfileServiceImpl)profileService).setPasswordEncoder(fakePasswordEncoder);
    }

    @Test
    public void shouldCreateNewProfile() {
        Profile p = new Profile(VALID_LOGIN, "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile saved = profileService.save(p);
        Assert.assertNotNull(saved.getProfileId());
    }

    @Test
    public void passwordMustBeEncoded() {
        ((ProfileServiceImpl)profileService).setPasswordEncoder(realPasswordEncoder);
        try {
            Profile p = new Profile(VALID_LOGIN, "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
            profileService.save(p);
            Profile saved = profileRepository.findByLogin(VALID_LOGIN);
            Assert.assertNotEquals(saved.getPass(), "s3cr3t", "password should not be equals to plain password informed");
        } finally {
            ((ProfileServiceImpl)profileService).setPasswordEncoder(fakePasswordEncoder);
        }
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

    @Test
    public void shouldChangePassword() {
        Profile p = new Profile(VALID_LOGIN, "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        profileService.save(p);

        profileService.changePassword(VALID_LOGIN, "s3cr3t", "newPassword", "newPassword");

        Profile queried = profileRepository.findByLogin(VALID_LOGIN);
        Assert.assertEquals(queried.getPass(), "newPassword", "password not updated as expected");
    }

    @Test
    public void passwordMustBeConfirmedOnChange() {
        Profile p = new Profile(VALID_LOGIN, "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        profileService.save(p);

        try {
            profileService.changePassword(VALID_LOGIN, "s3cr3t", "newPassword", "mispelledPassword");
            Assert.fail("should raised exception. password was not confirmed on change");
        } catch (ConstraintViolationException e) {
            new ExceptionChecker(e)
                    .assertErrorCount(1)
                    .assertTemplateMessagePresentAndI18Nized(
                            "{br.com.camiloporto.marmitex.microservice.profile.NEW_PASSWORD.notConfirmed}");
        }

        Profile queried = profileRepository.findByLogin(VALID_LOGIN);
        Assert.assertEquals(queried.getPass(), "s3cr3t", "password should not be changed");
    }

    @Test
    public void newPasswordIsRequiredOnChange() {
        Profile p = new Profile(VALID_LOGIN, "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        profileService.save(p);
        String newNullPassword = null;

        try {
            profileService.changePassword(VALID_LOGIN, "s3cr3t", newNullPassword, "mispelledPassword");
            Assert.fail("should raised exception. new password is required");
        } catch (ConstraintViolationException e) {
            new ExceptionChecker(e)
                    .assertErrorCount(1)
                    .assertTemplateMessagePresentAndI18Nized(
                            "{br.com.camiloporto.marmitex.microservice.profile.NEW_PASSWORD.required}");
        }

        Profile queried = profileRepository.findByLogin(VALID_LOGIN);
        Assert.assertEquals(queried.getPass(), "s3cr3t", "password should not be changed");
    }

    @Test
    public void confirmationPasswordIsRequiredOnChange() {
        Profile p = new Profile(VALID_LOGIN, "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        profileService.save(p);
        String newNullConfirmationPassword = null;

        try {
            profileService.changePassword(VALID_LOGIN, "s3cr3t", "newPassword", newNullConfirmationPassword);
            Assert.fail("should raised exception. confirmation password is required");
        } catch (ConstraintViolationException e) {
            new ExceptionChecker(e)
                    .assertErrorCount(1)
                    .assertTemplateMessagePresentAndI18Nized(
                            "{br.com.camiloporto.marmitex.microservice.profile.NEW_PASSWORD_CONFIRMATION.required}");
        }

        Profile queried = profileRepository.findByLogin(VALID_LOGIN);
        Assert.assertEquals(queried.getPass(), "s3cr3t", "password should not be changed");
    }

    //FIXME add change password function and rescue password. confirm pass
}

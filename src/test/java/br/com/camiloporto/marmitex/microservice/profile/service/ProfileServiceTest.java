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
        Profile p = new Profile("camiloporto", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile saved = profileService.save(p);
        Assert.assertNotNull(saved.getProfileId());
    }

    @Test
    public void profileLoginMustBeUnique() {
        Profile p1 = new Profile("camiloporto", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile p2 = new Profile("camiloporto", "sup3rs3cr3t", "Camilo Facanha", "1234-5674", "8th St.");
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
}

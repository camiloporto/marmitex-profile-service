package br.com.camiloporto.marmitex.microservice.profile.repository;

import br.com.camiloporto.marmitex.microservice.profile.ServiceApplication;
import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ur42 on 09/03/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServiceApplication.class)
public class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileService;

    @Test
    public void shouldCreateNewProfile() {
        Profile p = new Profile("camiloporto", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile saved = profileService.save(p);
        Assert.assertNotNull(saved.getId());
        Assert.assertNotNull(saved.getRevision());
    }
}

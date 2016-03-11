package br.com.camiloporto.marmitex.microservice.profile.repository;

import br.com.camiloporto.marmitex.microservice.profile.AbstractMarmitexProfileTest;
import br.com.camiloporto.marmitex.microservice.profile.ServiceApplication;
import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ur42 on 09/03/2016.
 */

public class ProfileRepositoryTest extends AbstractMarmitexProfileTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void shouldCreateNewProfile() {
        Profile p = new Profile("camiloporto", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile saved = profileRepository.save(p);
        Assert.assertNotNull(saved.getId());
        Assert.assertNotNull(saved.getRevision());
    }
}

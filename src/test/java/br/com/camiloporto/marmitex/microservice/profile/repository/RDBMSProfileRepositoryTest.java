package br.com.camiloporto.marmitex.microservice.profile.repository;

import br.com.camiloporto.marmitex.microservice.profile.AbstractTransactionalMarmitexProfileTest;
import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by ur42 on 28/03/2016.
 */
public class RDBMSProfileRepositoryTest extends AbstractTransactionalMarmitexProfileTest {

    @Autowired @Setter
    private RDMBSProfileRepository profileRepository;

    @Test
    public void shouldCreateNewProfile() {
        Profile p = new Profile("camiloporto", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile saved = profileRepository.save(p);
        Assert.assertNotNull(saved.getProfileId());

    }
}

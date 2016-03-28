package br.com.camiloporto.marmitex.microservice.profile.repository;

import br.com.camiloporto.marmitex.microservice.profile.AbstractMarmitexProfileTest;
import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ur42 on 28/03/2016.
 */
public class RDBMSProfileRepositoryTest extends AbstractMarmitexProfileTest {

    @Autowired @Setter
    private RDMBSProfileRepository profileRepository;

    @Test
    public void shouldCreateNewProfile() {
        Profile p = new Profile("camiloporto", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile saved = profileRepository.save(p);
        Assert.assertNotNull(saved.getProfileId());

    }

    @Test
    public void shouldFindByLoginPass() {

        Profile p = new Profile("shouldFindByLoginPass_login", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile saved = profileRepository.save(p);
        String savedId = saved.getId();

        Profile found = profileRepository.findByLoginAndPass("shouldFindByLoginPass_login", "s3cr3t");
        Assert.assertEquals(savedId, found.getId());
    }

}

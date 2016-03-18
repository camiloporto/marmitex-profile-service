package br.com.camiloporto.marmitex.microservice.profile.repository;

import br.com.camiloporto.marmitex.microservice.profile.AbstractMarmitexProfileTest;
import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.util.DatabaseCleaner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ur42 on 09/03/2016.
 */

public class ProfileRepositoryTest extends AbstractMarmitexProfileTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Before
    public void cleanProfileDatabase() {
        databaseCleaner.deleteAllProfileDocs();
    }

    @Test
    public void shouldCreateNewProfile() {
        Profile p = new Profile("camiloporto", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile saved = profileRepository.save(p);
        Assert.assertNotNull(saved.getId());
        Assert.assertNotNull(saved.getRevision());
    }

    //FIXME tornar testes repetiveis. Criar funcao para limpar docs criados no teste

    @Test
    public void shouldFindByLoginPass() {

        Profile p = new Profile("shouldFindByLoginPass_login", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile saved = profileRepository.save(p);
        String savedId = saved.getId();

        Profile found = profileRepository.findByLoginPass("shouldFindByLoginPass_login", "s3cr3t");
        Assert.assertEquals(savedId, found.getId());
    }
}

package br.com.camiloporto.marmitex.microservice.profile.repository;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.util.CloudantDatabaseCleaner;
import org.junit.Assert;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ur42 on 09/03/2016.
 */

public class CloudantProfileRepositoryTest/* extends AbstractTransactionalMarmitexProfileTest */{

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CloudantDatabaseCleaner databaseCleaner;

    @Before
    public void cleanProfileDatabase() {
        databaseCleaner.deleteAllProfileDocs();
    }

//    @Test
    public void shouldCreateNewProfile() {
        Profile p = new Profile("camiloporto", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile saved = profileRepository.save(p);
        Assert.assertNotNull(saved.getId());
        Assert.assertNotNull(saved.getRevision());
    }

//    @Test
    public void shouldUpdateExistentProfile() {

        //FIXME ao atualizar, como consultamndo pelo ID nao venha a semha.. ao atualizar.. a senha nao vai.. e a atualização apaga a senha. corrigir!!
        Profile p = new Profile("shouldUpdateExistentProfile_camiloporto", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        profileRepository.save(p);

        Profile saved = profileRepository.findById(p.getId());

        saved.setName("Camilo Porto Nunes");
        profileRepository.save(saved);

        Profile updated = profileRepository.findById(saved.getId());

        Assert.assertEquals(updated.getId(), saved.getId());
        Assert.assertTrue(updated.getRevision().startsWith("2"));
        Assert.assertEquals("Camilo Porto Nunes", updated.getName());
    }

//    @Test
    public void shouldFindByLogin() {

        Profile p = new Profile("shouldFindByLoginPass_login", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile saved = profileRepository.save(p);
        String savedId = saved.getId();

        Profile found = profileRepository.findById(savedId);
        Assert.assertEquals(savedId, found.getId());
        Assert.assertNotNull(found.getName());
        Assert.assertNotNull(found.getRevision());
        Assert.assertNotNull(found.getAddress());
        Assert.assertNotNull(found.getLogin());
        Assert.assertNull(found.getPass());
    }

//    @Test
    public void shouldFindByLoginPass() {

        Profile p = new Profile("shouldFindByLoginPass_login", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile saved = profileRepository.save(p);
        String savedId = saved.getId();

        Profile found = profileRepository.findByLoginPass("shouldFindByLoginPass_login", "s3cr3t");
        Assert.assertEquals(savedId, found.getId());
    }
}

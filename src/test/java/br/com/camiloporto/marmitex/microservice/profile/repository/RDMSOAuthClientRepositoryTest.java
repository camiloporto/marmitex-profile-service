package br.com.camiloporto.marmitex.microservice.profile.repository;

import br.com.camiloporto.marmitex.microservice.profile.AbstractTransactionalMarmitexProfileTest;
import br.com.camiloporto.marmitex.microservice.profile.model.OAuthClientProfile;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by ur42 on 12/04/2016.
 */
public class RDMSOAuthClientRepositoryTest extends AbstractTransactionalMarmitexProfileTest {

    @Autowired @Setter
    private RDMSOAuthClientProfileRepository clientRepository;

    @Test
    public void shouldCreateNewOAuthClientProfile() {
        OAuthClientProfile p = new OAuthClientProfile("camiloporto", "s3cr3t");
        clientRepository.save(p);

        Assert.assertNotNull(clientRepository.findOne("camiloporto"));

    }
}

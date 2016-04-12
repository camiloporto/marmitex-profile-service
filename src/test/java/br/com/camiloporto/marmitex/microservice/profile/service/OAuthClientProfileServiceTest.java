package br.com.camiloporto.marmitex.microservice.profile.service;

import br.com.camiloporto.marmitex.microservice.profile.AbstractMarmitexProfileTest;
import br.com.camiloporto.marmitex.microservice.profile.model.OAuthClientProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by ur42 on 12/04/2016.
 */
public class OAuthClientProfileServiceTest extends AbstractMarmitexProfileTest {

    @Autowired
    private OAuthClientProfileService clientProfileService;

    @Test
    public void shouldCreateNewOAuthClientProfile() {
        OAuthClientProfile p = new OAuthClientProfile("camiloporto", "s3cr3t");
        clientProfileService.create(p);

        Assert.assertNotNull(clientProfileService.loadClientByClientId("camiloporto"));

    }

    @Test
    public void shoudActAsClientDetailsService() {
        ClientDetailsService.class.isAssignableFrom(OAuthClientProfileServiceImpl.class);
        OAuthClientProfile p = new OAuthClientProfile("camiloporto", "s3cr3t");
        clientProfileService.create(p);

        ClientDetails clientDetails = clientProfileService.loadClientByClientId("camiloporto");
        Assert.assertNotNull(clientDetails);
    }

}

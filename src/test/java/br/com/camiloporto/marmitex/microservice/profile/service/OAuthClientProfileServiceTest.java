package br.com.camiloporto.marmitex.microservice.profile.service;

import br.com.camiloporto.marmitex.microservice.profile.AbstractMarmitexProfileTest;
import br.com.camiloporto.marmitex.microservice.profile.model.OAuthClientProfile;
import br.com.camiloporto.marmitex.microservice.profile.repository.RDMSOAuthClientProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by ur42 on 12/04/2016.
 */
public class OAuthClientProfileServiceTest extends AbstractMarmitexProfileTest {

    @Autowired
    private OAuthClientProfileService clientProfileService;

    @Autowired
    private RDMSOAuthClientProfileRepository clientProfileRepository;

    private PasswordEncoder realPasswordEncoder;
    private PasswordEncoder fakePasswordEncoder;

    @AfterClass
    public void unmockPasswordEncoder() {
        ((OAuthClientProfileServiceImpl)clientProfileService).setPasswordEncoder(realPasswordEncoder);
    }

    @BeforeClass
    public void mockPasswordEncoder() {
        realPasswordEncoder = ((OAuthClientProfileServiceImpl)clientProfileService).getPasswordEncoder();
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
        ((OAuthClientProfileServiceImpl)clientProfileService).setPasswordEncoder(fakePasswordEncoder);
    }

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

    @Test
    public void passwordMustBeEncoded() {
        ((OAuthClientProfileServiceImpl)clientProfileService).setPasswordEncoder(realPasswordEncoder);
        try {
            OAuthClientProfile p = new OAuthClientProfile("camiloporto", "s3cr3t");
            clientProfileService.create(p);
            ClientDetails saved = clientProfileService.loadClientByClientId("camiloporto");
            Assert.assertNotEquals(saved.getClientSecret(), "s3cr3t", "password should not be equals to plain password informed");
        } finally {
            ((OAuthClientProfileServiceImpl)clientProfileService).setPasswordEncoder(fakePasswordEncoder);
        }
    }

}

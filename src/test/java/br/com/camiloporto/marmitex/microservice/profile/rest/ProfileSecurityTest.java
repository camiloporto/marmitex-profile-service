package br.com.camiloporto.marmitex.microservice.profile.rest;

import br.com.camiloporto.marmitex.microservice.profile.AbstractMarmitexProfileTest;
import br.com.camiloporto.marmitex.microservice.profile.config.SecurityConfiguration;
import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by ur42 on 31/03/2016.
 */
@ContextConfiguration(classes =
        SecurityConfiguration.class)
@WebAppConfiguration
public class ProfileSecurityTest extends AbstractMarmitexProfileTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProfileService profileService;

    @BeforeClass
    public void setUp() throws Exception {
        mvc =  webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void shouldLoginExistentUser() throws Exception {
        String login = "camiloporto@email";
        String pass = "s3cr3t";
        Profile p = new Profile(login, pass, "Camilo Porto", "8888-8765", "5th St.");
        profileService.save(p);
        mvc.perform(
                formLogin("/login").user(login).password(pass))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated());
    }

    @Test
    public void passwordShouldBeEncrypted() throws Exception {
        String login = "camiloporto@email";
        String plainTextPassword = "s3cr3t";
        Profile p = new Profile(login, plainTextPassword, "Camilo Porto", "8888-8765", "5th St.");
        profileService.save(p);
        Profile queried = profileRepository.findByLogin(login);
        Assert.assertNotEquals(queried.getPass(), plainTextPassword, "password should be encrypted");
    }


    @Test
    public void shouldRejectWrongAuthentication() throws Exception {
        mvc.perform(
                formLogin("/login").user("user").password("wrongpass"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(unauthenticated());
    }
}

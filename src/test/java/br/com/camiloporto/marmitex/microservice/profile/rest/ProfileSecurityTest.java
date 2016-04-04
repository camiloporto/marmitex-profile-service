package br.com.camiloporto.marmitex.microservice.profile.rest;

import br.com.camiloporto.marmitex.microservice.profile.AbstractMarmitexProfileTest;
import br.com.camiloporto.marmitex.microservice.profile.config.SecurityConfiguration;
import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.service.ProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    public void shouldChangePassword() throws Exception {
        String login = "camiloporto@email";
        String pass = "s3cr3t";
        String newpass = "s3cr3t";
        Profile p = new Profile(login, pass, "Camilo Porto", "8888-8765", "5th St.");
        profileService.save(p);
        Profile saved = profileRepository.findByLogin("camiloporto@email");

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("username", login);
        jsonMap.put("password", pass);
        jsonMap.put("newPassword", newpass);
        jsonMap.put("confirmPassword", newpass);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(jsonMap);

        mvc.perform(
                post("/changePassword")
                        .with(user(login).password(pass)) //user must be authenticated to change pass
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());

        Profile updated = profileRepository.findByLogin("camiloporto@email");
        Assert.assertNotNull(updated.getPass());
        Assert.assertNotEquals(updated.getPass(), saved.getPass());

    }

    @Test
    public void changePasswordMustBeAuthenticated() throws Exception {

        String login = "camiloporto@email";
        String pass = "s3cr3t";
        String newpass = "s3cr3t";
        Profile p = new Profile(login, pass, "Camilo Porto", "8888-8765", "5th St.");
        profileService.save(p);

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("username", login);
        jsonMap.put("password", pass);
        jsonMap.put("newPassword", newpass);
        jsonMap.put("confirmPassword", newpass);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(jsonMap);

        mvc.perform(
                post("/changePassword")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isUnauthorized());


    }
}

package br.com.camiloporto.marmitex.microservice.profile.rest;

import br.com.camiloporto.marmitex.microservice.profile.AbstractMarmitexProfileTest;
import br.com.camiloporto.marmitex.microservice.profile.model.OAuthClientProfile;
import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.service.OAuthClientProfileService;
import br.com.camiloporto.marmitex.microservice.profile.service.ProfileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by ur42 on 01/03/2016.
 */
@WebAppConfiguration
public class ProfileRestTest extends AbstractMarmitexProfileTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private OAuthClientProfileService clientProfileService;

    @Autowired
    FilterChainProxy filterChain;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeClass
    public void setUp() throws Exception {
        mvc =  webAppContextSetup(webApplicationContext)
                .addFilters(filterChain)
                .build();
    }

    @Test
    public void shouldCreateNewProfile() throws Exception {
        Profile p = new Profile("camiloporto@email", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        String jsonContent = toJson(p);

        mvc.perform(MockMvcRequestBuilders
                .post("/uaa")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());

        Profile saved = profileRepository.findByLogin("camiloporto@email");
        Assert.assertNotNull(saved);

    }

    @Test
    public void shouldSendValidationExceptionsinJSONResponse() throws Exception {
        final String EMPTY_PASS = "";
        final String EMPTY_LOGIN = "";
        Profile p = new Profile(EMPTY_LOGIN, EMPTY_PASS, "Camilo Porto", "8888-8765", "5th St.");
        String jsonContent = toJson(p);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/uaa")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());

        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", not(emptyArray())));

    }

    @Test
    public void shouldHandleInternalErrors() throws Exception {

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/uaa")//GET not accepted
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());

        result
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.internalError").exists());

    }

    @Test
    public void resourceOwnersShouldNotLoginDirectly() throws Exception {
        String login = "camiloporto@email";
        String pass = "s3cr3t";
        Profile p = new Profile(login, pass, "Camilo Porto", "8888-8765", "5th St.");
        profileService.save(p);
        mvc.perform(
                formLogin("/login").user(login).password(pass))
                .andDo(print())
                .andExpect(status().isUnauthorized());
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
    public void shouldGetBearerToken() throws Exception {
        Profile user = new Profile("camiloporto@email.com", "user-s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        OAuthClientProfile client = new OAuthClientProfile("marmitex@email.com", "client-s3cr3t");
        profileService.save(user);
        clientProfileService.create(client);

        String header = "Basic " + new String(Base64.encode("marmitex@email.com:client-s3cr3t".getBytes()));
        MvcResult result = mvc
                .perform(post("/oauth/token")
                        .header("Authorization", header)
                        .param("grant_type", "password").param("scope", "write")
                        .param("username", "camiloporto@email.com").param("password", "user-s3cr3t"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        Object accessToken = this.objectMapper
                .readValue(result.getResponse().getContentAsString(), Map.class)
                .get("access_token");
        Assert.assertNotNull(accessToken);
    }

    @Test
    public void shouldChangePasswordWithAccessToken() throws Exception {
        Profile user = new Profile("camiloporto@email.com", "user-s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        OAuthClientProfile client = new OAuthClientProfile("marmitex@email.com", "client-s3cr3t");
        profileService.save(user);
        clientProfileService.create(client);

        String header = "Basic " + new String(Base64.encode("marmitex@email.com:client-s3cr3t".getBytes()));
        MvcResult result = mvc
                .perform(post("/oauth/token")
                        .header("Authorization", header)
                        .param("grant_type", "password").param("scope", "write")
                        .param("username", "camiloporto@email.com")
                        .param("password", "user-s3cr3t"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        Object accessToken = this.objectMapper
                .readValue(result.getResponse().getContentAsString(), Map.class)
                .get("access_token");

        String login = user.getLogin();
        String pass = "user-s3cr3t";
        String newpass = "newpass";
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("username", login);//FIXME should it pass login? get it from logged user session instead?
        jsonMap.put("password", pass);
        jsonMap.put("newPassword", newpass);
        jsonMap.put("confirmPassword", newpass);

        String jsonContent = objectMapper.writeValueAsString(jsonMap);

        header = "Bearer " + accessToken;
        mvc.perform(
                post("/changePassword")
                        .content(jsonContent)
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());

        Profile updated = profileRepository.findByLogin(login);
        Assert.assertNotNull(updated.getPass());
        Assert.assertNotEquals(updated.getPass(), pass);

        Assert.assertNotNull(accessToken);
    }

    private String toJson(Profile p) throws JsonProcessingException {
        return objectMapper.writeValueAsString(p);
    }

}

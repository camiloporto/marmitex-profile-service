package br.com.camiloporto.marmitex.microservice.profile.rest;

import br.com.camiloporto.marmitex.microservice.profile.AbstractMarmitexProfileTest;
import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.service.ProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by ur42 on 11/04/2016.
 */
//@ContextConfiguration(classes ={
//        SecurityConfiguration.class, OAuth2Configuration.class})
@WebIntegrationTest(randomPort = true)
public class OAuthRestTest extends AbstractMarmitexProfileTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProfileService profileService;

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
    public void shouldGetBearerToken() throws Exception {
        Profile user = new Profile("camiloporto@email.com", "user-s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        Profile client = new Profile("marmitex@email.com", "client-s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        profileService.save(user);
        profileService.save(client);

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
}

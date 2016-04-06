package br.com.camiloporto.marmitex.microservice.profile.rest;

import br.com.camiloporto.marmitex.microservice.profile.AbstractMarmitexProfileTest;
import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.not;
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

    @BeforeClass
    public void setUp() throws Exception {
        mvc =  webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void shouldCreateNewProfile() throws Exception {
        Profile p = new Profile("camiloporto@email", "s3cr3t", "Camilo Porto", "8888-8765", "5th St.");
        String jsonContent = toJson(p);

        mvc.perform(MockMvcRequestBuilders
                .post("/create")
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
                .post("/create")
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
                .get("/")//GET not accepted
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());

        result
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.internalError").exists());

    }

    private String toJson(Profile p) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(p);
    }

}

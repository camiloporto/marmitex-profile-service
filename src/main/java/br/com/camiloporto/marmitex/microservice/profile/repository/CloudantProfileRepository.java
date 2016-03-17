package br.com.camiloporto.marmitex.microservice.profile.repository;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.repository.cloudant.CloudantQuery;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by ur42 on 09/03/2016.
 */

@Component
public class CloudantProfileRepository implements ProfileRepository {

    //FIXME refatorar esse codigo. Parametrizar valores e delegar para o spring construir e injetar dependencias
    private String key = "hengledungsheriallestopa";
    private String pass = "4771147dce8dae2abf30787367d38c4197a39af7";
    private String endpoint = "https://camiloporto.cloudant.com/marmitex-dev";

    @Autowired
    private RestTemplate template;

    public CloudantProfileRepository() {}


    @Override
    public Profile save(Profile p) {
        String uuid = UUID.randomUUID().toString();
        HttpHeaders httpHeaders = prepareCloudantHttpRequestHeaders();

        HttpEntity<Profile> entity = new HttpEntity<Profile>(p, httpHeaders);

        ResponseEntity<CloudantCreateDocumentResponse> responseEntity = template.exchange(
                endpoint + "/" + uuid,
                HttpMethod.PUT,
                entity,
                CloudantCreateDocumentResponse.class
        );
        CloudantCreateDocumentResponse response = responseEntity.getBody();
        if(response.getStatus().equals("true")) {
            p.setId(response.getId());
            p.setRevision(response.getRev());
        } else {
            throw new RuntimeException(response.toString());
            //FIXME tratar erro
        }
        return p;
    }

    @Override
    public Profile findByLoginPass(String login, String pass) {
        HttpHeaders httpHeaders = prepareCloudantHttpRequestHeaders();

        CloudantQuery jsonQuery = createJsonQueryLoginPass(login, pass);
        String queryUrl = endpoint + "/_find";

        HttpEntity<CloudantQuery> entity = new HttpEntity<CloudantQuery>(jsonQuery, httpHeaders);
        ResponseEntity<CloudantQueryResponse<Profile>> responseEntity = template.exchange(
                queryUrl,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<CloudantQueryResponse<Profile>>() {});

        return responseEntity.getBody().getDocuments().get(0);
    }

    private CloudantQuery createJsonQueryLoginPass(String login, String pass) {

        CloudantQuery cloudantQuery = new CloudantQuery()
                .addSelector("login", login)
                .addSelector("pass", pass)
                .limit(1);

        return cloudantQuery;

    }

    private HttpHeaders prepareCloudantHttpRequestHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        String plainCreds = key + ":" + pass;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        httpHeaders.add("Authorization", "Basic " + base64Creds);
        return httpHeaders;
    }

    @Getter @Setter
    static class CloudantCreateDocumentResponse {

        @JsonProperty("ok")
        private String status;

        private String rev;

        private String id;
    }

    @Getter @Setter
    static class CloudantQueryResponse<T> {

        @JsonProperty("docs")
        private List<T> documents;
    }
}

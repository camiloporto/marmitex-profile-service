package br.com.camiloporto.marmitex.microservice.profile.util;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.repository.CloudantQueryResponse;
import br.com.camiloporto.marmitex.microservice.profile.repository.cloudant.CloudantQuery;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ur42 on 17/03/2016.
 */
@Component
public class DatabaseCleaner {

    //FIXME refatorar esse codigo. Parametrizar valores e delegar para o spring construir e injetar dependencias
    private String key = "hengledungsheriallestopa";

    private String pass = "4771147dce8dae2abf30787367d38c4197a39af7";
    private String endpoint = "https://camiloporto.cloudant.com/marmitex-dev";

    @Autowired
    private RestTemplate restTemplate;

    public void deleteAllProfileDocs() {
        List<Profile> allProfiles = queryAllProfileDocsForDelete();
        BulkDeleteRequest bdr = new BulkDeleteRequest(allProfiles);
        List<Profile> profilesDeleted = sendBulkDeleteRequest(bdr);
    }

    private List<Profile> sendBulkDeleteRequest(BulkDeleteRequest bdr) {
        HttpHeaders httpHeaders = prepareCloudantHttpRequestHeaders();

        String queryUrl = endpoint + "/_bulk_docs";

        HttpEntity<BulkDeleteRequest> entity = new HttpEntity<BulkDeleteRequest>(bdr, httpHeaders);
        ResponseEntity<List<Profile>> responseEntity = restTemplate.exchange(
                queryUrl,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<List<Profile>>() {});

        return responseEntity.getBody();
    }

    private List<Profile> queryAllProfileDocsForDelete() {
        HttpHeaders httpHeaders = prepareCloudantHttpRequestHeaders();

        String queryUrl = endpoint + "/_find";
        CloudantQuery cloudantQuery = new CloudantQuery()
                .addSelector("type", Profile.class.getName())
                .addFields("_id", "_rev", "type")
                .limit(1000);

        HttpEntity<CloudantQuery> entity = new HttpEntity<CloudantQuery>(cloudantQuery, httpHeaders);
        ResponseEntity<CloudantQueryResponse<Profile>> responseEntity = restTemplate.exchange(
                queryUrl,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<CloudantQueryResponse<Profile>>() {});

        return responseEntity.getBody().getDocuments();
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
}

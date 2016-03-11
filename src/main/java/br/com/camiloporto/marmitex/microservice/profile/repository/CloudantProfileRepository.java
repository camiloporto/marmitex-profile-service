package br.com.camiloporto.marmitex.microservice.profile.repository;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by ur42 on 09/03/2016.
 */

@Component
public class CloudantProfileRepository implements ProfileRepository {

    private String key = "hengledungsheriallestopa";
    private String pass = "4771147dce8dae2abf30787367d38c4197a39af7";
    private String endpoint = "https://camiloporto.cloudant.com/marmitex-dev";

    @Autowired
    private RestTemplate template;

    public CloudantProfileRepository() {
    }

    //FIXME ajustar mapeamento JSON para 'id' e 'rev' de profile.
    //FIXME refatorar esse codigo. Parametrizar valores e delegar para o spring construir e injetar dependencias

    @Override
    public Profile save(Profile p) {
        String uuid = UUID.randomUUID().toString();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        String plainCreds = key + ":" + pass;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        httpHeaders.add("Authorization", "Basic " + base64Creds);

        HttpEntity<Profile> entity = new HttpEntity<Profile>(p, httpHeaders);

        ResponseEntity<CloudantResponse> responseEntity = template.exchange(
                endpoint + "/" + uuid,
                HttpMethod.PUT,
                entity,
                CloudantResponse.class
        );
        CloudantResponse response = responseEntity.getBody();
        if(response.getStatus().equals("true")) {
            p.setId(response.getId());
            p.setRevision(response.getRev());
        } else {
            throw new RuntimeException(response.toString());
            //tratar erro
        }
        return p;
    }

    @Getter @Setter
    static class CloudantResponse {

        @JsonProperty("ok")
        private String status;

        private String rev;

        private String id;
    }
}

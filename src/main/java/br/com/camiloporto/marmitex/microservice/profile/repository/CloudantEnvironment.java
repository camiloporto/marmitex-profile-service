package br.com.camiloporto.marmitex.microservice.profile.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by ur42 on 21/03/2016.
 */
@Getter @Setter
//@Component
@Deprecated
public class CloudantEnvironment {

    @Value("${cloudant.key}")
    private String key;

    @Value("${cloudant.pass}")
    private String pass;

    @Value("${cloudant.endpoint}")
    private String endpoint;
}

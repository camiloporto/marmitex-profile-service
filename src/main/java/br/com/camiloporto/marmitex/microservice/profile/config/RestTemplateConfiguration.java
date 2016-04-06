package br.com.camiloporto.marmitex.microservice.profile.config;

import org.springframework.web.client.RestTemplate;

/**
 * Created by ur42 on 11/03/2016.
 */
@Deprecated
public interface RestTemplateConfiguration {

    RestTemplate createRestTemplate();
}

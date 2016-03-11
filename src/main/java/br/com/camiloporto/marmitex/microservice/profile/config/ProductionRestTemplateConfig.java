package br.com.camiloporto.marmitex.microservice.profile.config;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by ur42 on 11/03/2016.
 */
@Configuration
@Profile("production")
public class ProductionRestTemplateConfig implements RestTemplateConfiguration {

        @Bean
        public RestTemplate createRestTemplate() {
            return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        }
}

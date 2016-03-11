package br.com.camiloporto.marmitex.microservice.profile.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Created by ur42 on 11/03/2016.
 */
@Configuration
@Profile("behind-proxy")
public class RestTemplateBehindProxyConfig implements RestTemplateConfiguration {

    @Bean
    public RestTemplate createRestTemplate() {
        InetSocketAddress address = new InetSocketAddress(
                "localhost",3128);
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setProxy(new Proxy(Proxy.Type.HTTP, address));
        return new RestTemplate(factory);
    }
}

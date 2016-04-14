package br.com.camiloporto.marmitex.microservice.profile.config;

import br.com.camiloporto.marmitex.microservice.profile.security.RESTAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Created by ur42 on 13/04/2016.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RESTAuthenticationEntryPoint();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                    .antMatchers("/uaa").permitAll()
                .anyRequest().authenticated();
    }
}

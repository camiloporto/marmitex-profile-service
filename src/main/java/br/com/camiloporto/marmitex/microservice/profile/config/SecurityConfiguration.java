package br.com.camiloporto.marmitex.microservice.profile.config;

import br.com.camiloporto.marmitex.microservice.profile.security.RESTAuthenticationEntryPoint;
import br.com.camiloporto.marmitex.microservice.profile.security.RESTAuthenticationFailureHandler;
import br.com.camiloporto.marmitex.microservice.profile.security.RESTAuthenticationSuccessHandler;
import br.com.camiloporto.marmitex.microservice.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Created by ur42 on 31/03/2016.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private ProfileService profileService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                authenticationProvider(profileService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint())
            .and()
            .csrf().disable() //FIXME should it be disabled?
            .authorizeRequests()
                .antMatchers("/uaa").permitAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .successHandler(restAuthenticationSuccessHandler())
                .failureHandler(restAuthenticationFailureHandler())
                .permitAll();
    }

    @Bean
    public AuthenticationFailureHandler restAuthenticationFailureHandler() {
        return new RESTAuthenticationFailureHandler();
    }


    @Bean
    public AuthenticationSuccessHandler restAuthenticationSuccessHandler() {
        return new RESTAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RESTAuthenticationEntryPoint();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

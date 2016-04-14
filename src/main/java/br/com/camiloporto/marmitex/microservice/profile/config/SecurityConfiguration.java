package br.com.camiloporto.marmitex.microservice.profile.config;

import br.com.camiloporto.marmitex.microservice.profile.service.ProfileService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by ur42 on 31/03/2016.
 */
@Configuration
@EnableWebSecurity
@DependsOn(value = "passwordEncoder")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired @Setter
    private ProfileService profileService;

    @Autowired @Setter
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(profileService).passwordEncoder(passwordEncoder);
    }

}

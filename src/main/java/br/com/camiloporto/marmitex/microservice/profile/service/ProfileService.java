package br.com.camiloporto.marmitex.microservice.profile.service;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by ur42 on 29/03/2016.
 */
public interface ProfileService extends AuthenticationProvider {

    Profile save(Profile p);

    @Override
    Authentication authenticate(Authentication authentication) throws AuthenticationException;

    @Override
    boolean supports(Class<?> aClass);

    void changePassword(String username, String actualPassword, String newPassword);
}

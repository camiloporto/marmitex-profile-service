package br.com.camiloporto.marmitex.microservice.profile.service;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by ur42 on 29/03/2016.
 */
public interface ProfileService extends UserDetailsService {

    Profile save(Profile p);

    void changePassword(String username, String actualPassword, String newPassword, String confirmedPassword);
}

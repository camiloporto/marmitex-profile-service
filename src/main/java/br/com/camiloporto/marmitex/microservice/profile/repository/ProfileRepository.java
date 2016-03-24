package br.com.camiloporto.marmitex.microservice.profile.repository;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;

/**
 * Created by ur42 on 09/03/2016.
 */
public interface ProfileRepository {
    Profile save(Profile p);

    Profile findByLoginPass(String login, String pass);

    Profile findById(String id);
}

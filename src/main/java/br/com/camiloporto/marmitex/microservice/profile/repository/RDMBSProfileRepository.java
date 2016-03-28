package br.com.camiloporto.marmitex.microservice.profile.repository;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ur42 on 28/03/2016.
 */
@Repository
public interface RDMBSProfileRepository extends JpaRepository<Profile, Long> {

    Profile findByLoginAndPass(String login, String pass);
}

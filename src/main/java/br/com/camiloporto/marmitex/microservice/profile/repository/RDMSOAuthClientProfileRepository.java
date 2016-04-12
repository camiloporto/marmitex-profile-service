package br.com.camiloporto.marmitex.microservice.profile.repository;

import br.com.camiloporto.marmitex.microservice.profile.model.OAuthClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ur42 on 12/04/2016.
 */
@Repository
public interface RDMSOAuthClientProfileRepository extends JpaRepository<OAuthClientProfile, String> {
}

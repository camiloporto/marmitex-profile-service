package br.com.camiloporto.marmitex.microservice.profile.service;

import br.com.camiloporto.marmitex.microservice.profile.model.OAuthClientProfile;
import org.springframework.security.oauth2.provider.ClientDetailsService;

/**
 * Created by ur42 on 12/04/2016.
 */
public interface OAuthClientProfileService extends ClientDetailsService {

    void create(OAuthClientProfile p);

}

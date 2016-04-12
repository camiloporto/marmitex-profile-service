package br.com.camiloporto.marmitex.microservice.profile.service;

import br.com.camiloporto.marmitex.microservice.profile.model.OAuthClientProfile;
import br.com.camiloporto.marmitex.microservice.profile.repository.RDMSOAuthClientProfileRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by ur42 on 12/04/2016.
 */
@Service
public class OAuthClientProfileServiceImpl implements OAuthClientProfileService {

    @Autowired
    private RDMSOAuthClientProfileRepository clientProfileRepository;

    @Autowired @Setter
    @Getter(value = AccessLevel.PACKAGE)
    private PasswordEncoder passwordEncoder;

    @Override
    public void create(OAuthClientProfile p) {
        //FIXME encrypt password with BCrypt
        String encodedPass = passwordEncoder.encode(p.getPassword());
        p.setPassword(encodedPass);
        clientProfileRepository.save(p);
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        OAuthClientProfile clientProfile = clientProfileRepository.findOne(clientId);

        //FIXME put these infos in database, as needed
        BaseClientDetails clientDetails = new BaseClientDetails(
                clientProfile.getUsername(),
                "", "write", "password", "");

        clientDetails.setClientSecret(clientProfile.getPassword());
        clientDetails.setAuthorizedGrantTypes(Arrays.asList("password"));
        return clientDetails;
    }
}

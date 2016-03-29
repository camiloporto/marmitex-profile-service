package br.com.camiloporto.marmitex.microservice.profile.service;

import br.com.camiloporto.marmitex.microservice.profile.repository.RDMBSProfileRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ur42 on 29/03/2016.
 */
@Component
public class ChecklistValidationFactory {

    @Autowired @Setter
    private RDMBSProfileRepository profileRepository;

    public ProfileValidationChecklist createProfileValidationChecklist() {
        return new ProfileValidationChecklist(profileRepository);
    }
}

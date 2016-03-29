package br.com.camiloporto.marmitex.microservice.profile.service;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.repository.RDMBSProfileRepository;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;

/**
 * Created by ur42 on 29/03/2016.
 */
public class ProfileValidationChecklist {

    public interface NewProfileRuleGroups {}

    private RDMBSProfileRepository profileRepository;

    @Setter
    private Profile profile;

    public ProfileValidationChecklist(RDMBSProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @AssertTrue(groups = NewProfileRuleGroups.class,
            message = "{br.com.camiloporto.marmitex.microservice.profile.LOGIN_UNIQUE}")
    public boolean isLoginUnique() {
        Profile p = profileRepository.findByLogin(profile.getLogin());
        return p == null;
    }
}

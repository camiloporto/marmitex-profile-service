package br.com.camiloporto.marmitex.microservice.profile.service;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.repository.RDMBSProfileRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validator;

/**
 * Created by ur42 on 29/03/2016.
 */
@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    @Setter
    private Validator validator;

    @Autowired
    @Setter
    private RDMBSProfileRepository profileRepository;

    @Autowired @Setter
    private ChecklistValidationFactory checklistValidationFactory;

    @Override
    public Profile save(Profile p) {
        BusinessValidator<ProfileValidationChecklist> businessValidator =
                new BusinessValidator<ProfileValidationChecklist>(validator);
        ProfileValidationChecklist checklist = checklistValidationFactory.createProfileValidationChecklist();
        checklist.setProfile(p);
        businessValidator.validate(checklist, ProfileValidationChecklist.NewProfileRuleGroups.class);
        Profile saved = profileRepository.save(p);
        return saved;
    }
}

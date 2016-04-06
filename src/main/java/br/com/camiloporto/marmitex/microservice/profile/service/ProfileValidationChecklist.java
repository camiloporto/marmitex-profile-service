package br.com.camiloporto.marmitex.microservice.profile.service;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.repository.RDMBSProfileRepository;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

/**
 * Created by ur42 on 29/03/2016.
 */
public class ProfileValidationChecklist {

    public interface NewProfileRuleGroups {}
    public interface ChangePasswordRuleGroups {}

    private RDMBSProfileRepository profileRepository;

    @Setter
    @Valid
    private Profile profile;

    @Setter
    @NotEmpty(groups = ChangePasswordRuleGroups.class,
        message = "{br.com.camiloporto.marmitex.microservice.profile.NEW_PASSWORD.required}")
    private String newPassword;

    @Setter
    @NotEmpty(groups = ChangePasswordRuleGroups.class,
            message = "{br.com.camiloporto.marmitex.microservice.profile.NEW_PASSWORD_CONFIRMATION.required}")
    private String confirmedPassword;

    public ProfileValidationChecklist(RDMBSProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @AssertTrue(groups = NewProfileRuleGroups.class,
            message = "{br.com.camiloporto.marmitex.microservice.profile.LOGIN_UNIQUE}")
    public boolean isLoginUnique() {
        Profile p = profileRepository.findByLogin(profile.getLogin());
        return p == null;
    }

    @AssertTrue(groups = ChangePasswordRuleGroups.class,
        message = "{br.com.camiloporto.marmitex.microservice.profile.NEW_PASSWORD.notConfirmed}")
    public boolean isNewPasswordConfirmed() {
        if(newPassword != null && confirmedPassword != null) {
            return newPassword.equals(confirmedPassword);
        }
        return true;//rule not applied if either passwords are null.
    }


}

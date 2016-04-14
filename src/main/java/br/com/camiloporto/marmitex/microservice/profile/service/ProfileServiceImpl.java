package br.com.camiloporto.marmitex.microservice.profile.service;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.repository.RDMBSProfileRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ur42 on 29/03/2016.
 */
@Service
@DependsOn(value = "passwordEncoder")
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    @Setter
    private Validator validator;

    @Autowired
    @Setter
    private RDMBSProfileRepository profileRepository;

    @Autowired @Setter
    private ChecklistValidationFactory checklistValidationFactory;

    @Autowired @Setter @Getter(value = AccessLevel.PACKAGE)
    private PasswordEncoder passwordEncoder;

    @Override
    public Profile save(Profile p) {
        BusinessValidator<ProfileValidationChecklist> businessValidator =
                new BusinessValidator<>(validator);
        ProfileValidationChecklist checklist = checklistValidationFactory.createProfileValidationChecklist();
        checklist.setProfile(p);
        businessValidator.validate(checklist, ProfileValidationChecklist.NewProfileRuleGroups.class);
        encryptPassword(p);
        return profileRepository.save(p);
    }

    private void encryptPassword(Profile p) {
        String encodedPass = passwordEncoder.encode(p.getPass());
        p.setPass(encodedPass);
    }

    private List<GrantedAuthority> loadAuthorities(Profile profile) {
        //FIXME load authorities when necessary
        return new ArrayList<>();
    }

    @Override
    public void changePassword(String login, String actualPassword, String newPassword, String confirmedPassword) {
        Profile profile = profileRepository.findByLogin(login);
        BusinessValidator<ProfileValidationChecklist> businessValidator =
                new BusinessValidator<>(validator);
        ProfileValidationChecklist checklist = checklistValidationFactory.createProfileValidationChecklist();
        checklist.setProfile(profile);
        checklist.setNewPassword(newPassword);
        checklist.setConfirmedPassword(confirmedPassword);
        businessValidator.validate(checklist, ProfileValidationChecklist.ChangePasswordRuleGroups.class);

        if(profile != null && passwordEncoder.matches(actualPassword, profile.getPass())) {
            String encodedPass = passwordEncoder.encode(newPassword);
            profile.setPass(encodedPass);
            profileRepository.save(profile);
        } else {
            //FIXME throw more adequate exception.. CVEx?
            throw new BadCredentialsException("Bad Credentials");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Profile profile = profileRepository.findByLogin(login);
        if(profile != null) {
            List<GrantedAuthority> authorities = loadAuthorities(profile);
            return new User(profile.getLogin(), profile.getPass(), authorities);
        } else {
            throw new UsernameNotFoundException("login");
        }
    }
}

package br.com.camiloporto.marmitex.microservice.profile.service;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.repository.RDMBSProfileRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String plainPass = authentication.getCredentials().toString();
        Profile profile = profileRepository.findByLogin(login);
        if(profile != null && passwordEncoder.matches(plainPass, profile.getPass())) {
            List<GrantedAuthority> authorities = loadAuthorities(profile);
            return new UsernamePasswordAuthenticationToken(login, plainPass, authorities);
        } else {
            throw new BadCredentialsException("Bad Credentials");
        }
    }

    private List<GrantedAuthority> loadAuthorities(Profile profile) {
        //FIXME load authorities when necessary
        return new ArrayList<>();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    public void changePassword(String login, String actualPassword, String newPassword) {
        Profile profile = profileRepository.findByLogin(login);
        if(profile != null && passwordEncoder.matches(actualPassword, profile.getPass())) {
            String encodedPass = passwordEncoder.encode(newPassword);
            profile.setPass(encodedPass);
            profileRepository.save(profile);
        } else {
            //FIXME throw more adequate exception.. CVEx?
            throw new BadCredentialsException("Bad Credentials");
        }
    }
}

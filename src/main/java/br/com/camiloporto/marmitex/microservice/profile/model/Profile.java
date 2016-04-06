package br.com.camiloporto.marmitex.microservice.profile.model;

import br.com.camiloporto.marmitex.microservice.profile.service.ProfileValidationChecklist;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * Created by ur42 on 09/03/2016.
 */

@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "Profile")
public class Profile {

    @Transient
    private String type = Profile.class.getName();

    @Transient
    @JsonProperty("_rev")
    private String revision;

    @Transient
//    @JsonProperty("_id")
    private String id;

    @Id
    @SequenceGenerator(name = "profile_sq", sequenceName = "profile_sq")
    @GeneratedValue(generator = "profile_sq", strategy = GenerationType.AUTO)
    @JsonProperty("_id")
    private Long profileId;

    @Column(name = "login")
    @Email(message = "{br.com.camiloporto.marmitex.microservice.profile.INVALID_LOGIN}",
        groups = ProfileValidationChecklist.NewProfileRuleGroups.class)
    @NotEmpty(message= "{br.com.camiloporto.marmitex.microservice.profile.LOGIN_REQUIRED}",
         groups= ProfileValidationChecklist.NewProfileRuleGroups.class)
    private String login;

    @Column(name = "pass")
    @NotEmpty(message= "{br.com.camiloporto.marmitex.microservice.profile.PASSWORD_REQUIRED}",
            groups= ProfileValidationChecklist.NewProfileRuleGroups.class)
    private String pass;

    @Transient
    private String name;

    @Transient
    private String phoneNumber;

    @Transient
    private String address;

    public Profile(){}

    public Profile(String login, String pass, String name, String phoneNumber, String address) {

        this.login = login;
        this.pass = pass;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}

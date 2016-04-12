package br.com.camiloporto.marmitex.microservice.profile.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by ur42 on 12/04/2016.
 */
@Getter @Setter
@Entity
@Table(name = "OAuthClientProfile")
public class OAuthClientProfile {

    @Id
    private String username;
    private String password;

    public OAuthClientProfile() {
    }

    public OAuthClientProfile(String username, String password) {

        this.username = username;
        this.password = password;
    }
}

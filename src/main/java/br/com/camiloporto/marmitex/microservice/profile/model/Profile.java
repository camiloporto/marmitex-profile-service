package br.com.camiloporto.marmitex.microservice.profile.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ur42 on 09/03/2016.
 */

@Getter @Setter
public class Profile {

    private String login;
    private String pass;
    private String name;
    private String phoneNumber;
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

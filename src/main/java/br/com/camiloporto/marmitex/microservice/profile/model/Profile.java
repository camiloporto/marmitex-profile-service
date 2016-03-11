package br.com.camiloporto.marmitex.microservice.profile.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by ur42 on 09/03/2016.
 */

@Getter @Setter
public class Profile {

    @SerializedName("_id")
    private String id;

    @SerializedName("_rev")
    private String revision;

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

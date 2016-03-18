package br.com.camiloporto.marmitex.microservice.profile.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by ur42 on 09/03/2016.
 */

@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Profile {

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
    private String type;

    @JsonProperty("_id")
    private String id;

    @JsonProperty("_rev")
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

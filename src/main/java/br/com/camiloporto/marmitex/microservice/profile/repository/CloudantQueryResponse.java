package br.com.camiloporto.marmitex.microservice.profile.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by ur42 on 17/03/2016.
 */
@Getter
@Setter
public class CloudantQueryResponse<T> {

    @JsonProperty("docs")
    private List<T> documents;
}

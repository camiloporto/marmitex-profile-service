package br.com.camiloporto.marmitex.microservice.profile.repository.cloudant;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ur42 on 15/03/2016.
 */
public class CloudantQuery {

    @Getter
    @JsonProperty("selector")
    private Map<String, String> selector = new HashMap<>();

    @Getter
    @JsonProperty("limit")
    private Integer limit;

    public CloudantQuery addSelector(String attribute, String value) {
        selector.put(attribute, value);
        return this;
    }

    public CloudantQuery limit(Integer limit) {
        this.limit = limit;
        return this;
    }

}

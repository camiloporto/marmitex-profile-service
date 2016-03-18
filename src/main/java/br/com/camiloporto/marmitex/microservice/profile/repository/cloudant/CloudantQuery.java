package br.com.camiloporto.marmitex.microservice.profile.repository.cloudant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ur42 on 15/03/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CloudantQuery {

    @Getter
    @JsonProperty("selector")
    private Map<String, String> selector = new HashMap<>();

    @Getter
    @JsonProperty("fields")
    private List<String> fields;

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

    public CloudantQuery addFields(String... fieldsToAdd) {
        if(fields == null) fields = new ArrayList();
        for (String field: fieldsToAdd) {
            fields.add(field);
        }
        return this;
    }
}

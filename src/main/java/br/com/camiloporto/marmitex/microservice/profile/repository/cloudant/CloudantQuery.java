package br.com.camiloporto.marmitex.microservice.profile.repository.cloudant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    public CloudantQuery addAll(Class<?> t) {
        if(fields == null) fields = new ArrayList();
        Field[] classFields = t.getDeclaredFields();
        for (Field f : classFields) {
            boolean hasJsonAnnotation = false;
            Annotation[] fieldAnnotations = f.getDeclaredAnnotations();
            for (Annotation a :fieldAnnotations) {
                Class<? extends Annotation> annotationType = a.annotationType();
                String name = annotationType.getCanonicalName();
                String canonicalName = JsonProperty.class.getCanonicalName();
                if(canonicalName.equals(name)) {
                    try {
                        Method[] methods = annotationType.getMethods();
                        Method valueMethod = annotationType.getMethod("value", null);
                        Object result = valueMethod.invoke(a);
                        fields.add((String) result);
                        hasJsonAnnotation = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if(!hasJsonAnnotation) {
                fields.add(f.getName());
            }
        }
        return this;
    }

    public CloudantQuery addFields(String... fieldsToAdd) {
        if(fields == null) fields = new ArrayList();
        for (String field: fieldsToAdd) {
            fields.add(field);
        }
        return this;
    }

    public CloudantQuery excludeFields(String... fieldsToRemove) {
        for (String field: fieldsToRemove) {
            fields.remove(field);
        }
        return this;
    }
}

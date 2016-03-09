package br.com.camiloporto.marmitex.microservice.profile.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ur42 on 04/03/2016.
 */
@RestController("/")
public class ProfileRest {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    String hello() {
        return "{hello : ola camilo}";
    }
}

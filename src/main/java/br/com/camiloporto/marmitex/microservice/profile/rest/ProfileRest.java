package br.com.camiloporto.marmitex.microservice.profile.rest;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ur42 on 04/03/2016.
 */
@RestController("/")
public class ProfileRest {

    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
        )
    public @ResponseBody String createProfile(@RequestBody Profile profile) {
        return "{hello : ola camilo}";
    }
}

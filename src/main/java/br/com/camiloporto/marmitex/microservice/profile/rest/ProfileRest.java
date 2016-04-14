package br.com.camiloporto.marmitex.microservice.profile.rest;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import br.com.camiloporto.marmitex.microservice.profile.service.ProfileService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * Created by ur42 on 04/03/2016.
 */
@RestController
public class ProfileRest {


    @Autowired @Setter
    private ProfileService profileService;

    //FIXME only registered Clients should call this API?
    @RequestMapping(value = "/uaa",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
        )
    public @ResponseBody void createProfile(@RequestBody Profile profile) {
        profileService.save(profile);
    }

    @RequestMapping(
            value = "/changePassword",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
        )
    public @ResponseBody void changePassword(Principal principal, @RequestBody Map<String, String> params) {
        String username = principal.getName();
        String password = params.get("password");
        String newPassword = params.get("newPassword");
        String confirmPassword = params.get("confirmPassword");
        profileService.changePassword(username, password, newPassword, confirmPassword);
    }
}

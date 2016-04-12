package br.com.camiloporto.marmitex.microservice.profile.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * Created by ur42 on 08/04/2016.
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Configuration /*extends AuthorizationServerConfigurerAdapter */{

    //FIXME criar um cadastro de clientes
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.authenticationManager(authenticationManager);
//    }

//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                .withClient("marmitex")
//                .secret("marmitex-secret")
//                .authorizedGrantTypes("password")
//                .scopes("write");
//    }
}

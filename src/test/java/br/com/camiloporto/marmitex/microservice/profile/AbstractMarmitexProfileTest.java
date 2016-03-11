package br.com.camiloporto.marmitex.microservice.profile;

import br.com.camiloporto.marmitex.microservice.profile.config.RestTemplateBehindProxyConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ur42 on 11/03/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ServiceApplication.class, RestTemplateBehindProxyConfig.class})
@ActiveProfiles(profiles = "behind-proxy")
public abstract class AbstractMarmitexProfileTest {}

package br.com.camiloporto.marmitex.microservice.profile;

import br.com.camiloporto.marmitex.microservice.profile.config.RestTemplateBehindProxyConfig;
import br.com.camiloporto.marmitex.microservice.profile.config.TestDataSourceConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

/**
 * Created by ur42 on 11/03/2016.
 */
@SpringApplicationConfiguration(classes = {ServiceApplication.class, RestTemplateBehindProxyConfig.class, TestDataSourceConfiguration.class})
public abstract class AbstractTransactionalMarmitexProfileTest extends AbstractTransactionalTestNGSpringContextTests {}

package br.com.camiloporto.marmitex.microservice.profile;

import br.com.camiloporto.marmitex.microservice.profile.config.RestTemplateBehindProxyConfig;
import br.com.camiloporto.marmitex.microservice.profile.config.TestDataSourceConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * Created by ur42 on 29/03/2016.
 */
@SpringApplicationConfiguration(classes = {ServiceApplication.class, RestTemplateBehindProxyConfig.class, TestDataSourceConfiguration.class})
public class AbstractMarmitexProfileTest extends AbstractTestNGSpringContextTests {
}

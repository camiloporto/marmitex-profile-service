package br.com.camiloporto.marmitex.microservice.profile;

import br.com.camiloporto.marmitex.microservice.profile.config.RestTemplateBehindProxyConfig;
import br.com.camiloporto.marmitex.microservice.profile.config.TestDataSourceConfiguration;
import br.com.camiloporto.marmitex.microservice.profile.repository.RDMBSProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;

/**
 * Created by ur42 on 29/03/2016.
 */
@SpringApplicationConfiguration(classes = {
        ServiceApplication.class,
        RestTemplateBehindProxyConfig.class,
        TestDataSourceConfiguration.class
        })
public class AbstractMarmitexProfileTest extends AbstractTestNGSpringContextTests {

        @Autowired
        protected RDMBSProfileRepository profileRepository;

        @BeforeMethod
        public void deleteData() {
                profileRepository.deleteAll();
        }
}

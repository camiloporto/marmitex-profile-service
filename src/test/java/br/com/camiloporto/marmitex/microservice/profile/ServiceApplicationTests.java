package br.com.camiloporto.marmitex.microservice.profile;

import br.com.camiloporto.marmitex.microservice.profile.repository.ProfileRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@WebAppConfiguration
public class ServiceApplicationTests extends AbstractMarmitexProfileTest {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ProfileRepository profileService;

	@Test
	public void contextLoads() {
		Assert.assertNotNull(restTemplate);
		Assert.assertNotNull(profileService);
	}

}

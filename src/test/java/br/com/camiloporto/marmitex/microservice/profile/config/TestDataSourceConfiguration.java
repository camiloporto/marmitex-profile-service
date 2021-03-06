package br.com.camiloporto.marmitex.microservice.profile.config;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by ur42 on 28/03/2016.
 */
@Configuration
@Profile("dev, travis")
@EnableJpaRepositories(basePackages = "br.com.camiloporto.marmitex.microservice.profile")
@EntityScan(basePackages = "br.com.camiloporto.marmitex.microservice.profile")
public class TestDataSourceConfiguration {
}

package br.com.camiloporto.marmitex.microservice.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}
//
//	@Autowired
//	private DataSourceConfiguration dataSourceConfiguration;
//
//	@Bean
//	public EntityManagerFactory entityManagerFactory() {
//
//		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//		vendorAdapter.setGenerateDdl(true);
//
//		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//		factory.setJpaVendorAdapter(vendorAdapter);
//		factory.setPackagesToScan("br.com.camiloporto.marmitex.microservice.profile");
//		factory.setDataSource(dataSourceConfiguration.createDataSource());
//		factory.afterPropertiesSet();
//
//		return factory.getObject();
//	}
//
//	@Bean
//	public PlatformTransactionManager transactionManager() {
//
//		JpaTransactionManager txManager = new JpaTransactionManager();
//		txManager.setEntityManagerFactory(entityManagerFactory());
//		return txManager;
//	}

}

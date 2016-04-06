package br.com.camiloporto.marmitex.microservice.profile.config;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * Created by ur42 on 05/04/2016.
 */
//@Configuration
//@Profile("heroku")
public class HerokuDataSourceConfiguration implements DataSourceConfiguration {


    @Override
//    @Bean
    public DataSource createDataSource() {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        String username = System.getenv("JDBC_DATABASE_USERNAME");
        String password = System.getenv("JDBC_DATABASE_PASSWORD");

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setConnectionTestQuery("SELECT 1");
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}

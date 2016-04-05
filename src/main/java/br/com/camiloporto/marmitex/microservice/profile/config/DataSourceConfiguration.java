package br.com.camiloporto.marmitex.microservice.profile.config;

import javax.sql.DataSource;

/**
 * Created by ur42 on 28/03/2016.
 */
public interface DataSourceConfiguration {

    public DataSource createDataSource();
}

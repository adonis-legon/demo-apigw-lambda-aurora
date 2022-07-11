package com.example.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class CustomerDatasource {
    @Autowired
    private CustomerDatasourceProperties customerDatasourceProperties;

    @Bean
    public DataSource documentDataSourceCL() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUsername(customerDatasourceProperties.getUsername());
        dataSource.setPassword(customerDatasourceProperties.getPassword());
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(String.format("jdbc:postgresql://%s:%s/%s", customerDatasourceProperties.getHost(),
                Integer.toString(customerDatasourceProperties.getPort()), customerDatasourceProperties.getDbName()));

        return dataSource;
    }
}

package com.example.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.amazonaws.xray.sql.TracingDataSource;
import com.zaxxer.hikari.HikariConfig;

@Configuration
public class CustomerDatasource {
    @Autowired
    private CustomerDatasourceProperties customerDatasourceProperties;

    @Bean
    public DataSource customerDataSource() {
        String connDriver = "org.postgresql.Driver";
        String connString = String.format("jdbc:postgresql://%s:%s/%s", customerDatasourceProperties.getHost(),
                Integer.toString(customerDatasourceProperties.getPort()), customerDatasourceProperties.getDbName());

        if (customerDatasourceProperties.getAuthIam()) {
            HikariConfig dataSourceConfig = new HikariConfig();

            dataSourceConfig.setUsername(customerDatasourceProperties.getUsername());
            dataSourceConfig.setDriverClassName(connDriver);
            dataSourceConfig.setJdbcUrl(connString + "?ssl=true,sslMode=verify-full,sslfactory=org.postgresql.ssl.DefaultJavaSSLFactory");

            RdsIamHikariDataSource dataSource = new RdsIamHikariDataSource(customerDatasourceProperties.getAwsRegion());
            dataSourceConfig.copyStateTo(dataSource);

            return new TracingDataSource(dataSource);
        } else {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();

            dataSource.setUsername(customerDatasourceProperties.getUsername());
            dataSource.setPassword(customerDatasourceProperties.getPassword());
            dataSource.setDriverClassName(connDriver);
            dataSource.setUrl(connString);

            return dataSource;
        }
    }
}

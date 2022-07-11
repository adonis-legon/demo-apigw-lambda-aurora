package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.datasource")
public class CustomerDatasourceProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private String dbName;
    private Boolean authIam;
    private String awsRegion;
}

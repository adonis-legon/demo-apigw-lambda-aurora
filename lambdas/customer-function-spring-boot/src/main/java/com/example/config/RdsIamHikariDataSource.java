package com.example.config;

import org.springframework.data.util.Pair;

import com.zaxxer.hikari.HikariDataSource;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsUtilities;
import software.amazon.awssdk.services.rds.model.GenerateAuthenticationTokenRequest;

public class RdsIamHikariDataSource extends HikariDataSource{

    private String awsRegion;

    public RdsIamHikariDataSource(String awsRegion) {
        super();
        this.awsRegion = awsRegion;
    }

    @Override
    public String getPassword() {
        return getToken();
    }

    private String getToken() {
        var hostnamePort = getHostnamePort();

        RdsUtilities utilities = RdsUtilities.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(this.awsRegion))
                .build();

        GenerateAuthenticationTokenRequest authTokenRequest = GenerateAuthenticationTokenRequest.builder()
                .username(getUsername())
                .hostname(hostnamePort.getFirst())
                .port(hostnamePort.getSecond())
                .build();

        return utilities.generateAuthenticationToken(authTokenRequest);
    }

    private Pair<String, Integer> getHostnamePort() {
        var slashing = getJdbcUrl().indexOf("//") + 2;
        var sub = getJdbcUrl().substring(slashing, getJdbcUrl().indexOf("/", slashing));
        var splitted = sub.split(":");
        return Pair.of(splitted[0], Integer.parseInt(splitted[1]));
    }
}

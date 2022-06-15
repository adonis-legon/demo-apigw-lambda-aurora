package com.example.customerfunction.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsUtilities;
import software.amazon.awssdk.services.rds.model.GenerateAuthenticationTokenRequest;
import software.amazon.lambda.powertools.tracing.Tracing;

@Slf4j
public class DbManager {
    private static final String JDBC_PREFIX = "jdbc:postgresql://";
    private static final String DB_ENDPOINT = System.getenv("DB_ENDPOINT");
    private static final Integer DB_PORT;
    private static final String DB_REGION = System.getenv("DB_REGION");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASS = System.getenv("DB_PASS");
    private static final String DB_NAME = System.getenv("DB_NAME");
    private static final String DB_AUTH_IAM = System.getenv("DB_AUTH_IAM");
    private static final long BACKOFF_TIME_MILLI = 1000;

    @Getter
    private Connection dbConnection;

    static {
        DB_PORT = retrievePort("DB_PORT", 5432);
    }

    @Tracing(segmentName = "CreateDBConnection")
    public Connection createConnection() {
        try {
            boolean iamAuth = DB_AUTH_IAM != null && !DB_AUTH_IAM.isEmpty() && DB_AUTH_IAM.equalsIgnoreCase("true");

            Properties dbConnectionProperties = new Properties();
            dbConnectionProperties.setProperty("user", DB_USER);
            dbConnectionProperties.setProperty("password",
                    getUserPassword(DB_USER, DB_ENDPOINT, DB_REGION, DB_PORT, iamAuth));

            String dbUrl = String.format("%s%s:%d/%s", JDBC_PREFIX, DB_ENDPOINT, DB_PORT, DB_NAME);

            this.dbConnection = DriverManager.getConnection(dbUrl, dbConnectionProperties);
            log.info("Connection Established");
        } catch (Exception e) {
            log.error("Connection FAILED. Message: " + e.getMessage(), e);
            this.dbConnection = null;
        }

        return this.dbConnection;
    }

    @Tracing(segmentName = "RefreshDBConnection")
    protected Connection refreshDbConnection() {
        try {
            if (this.dbConnection == null || !this.dbConnection.isValid(1)) {
                log.info("Retrying database connection");
                try {
                    Thread.sleep(BACKOFF_TIME_MILLI);
                    this.dbConnection = this.createConnection();
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(
                            "There was a problem sleeping the thread while creating a connection to the DB");
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(
                    "There was a problem refreshing the database connection due to an error while checking validity");
        }

        return this.dbConnection;
    }

    private String getUserPassword(String username, String dbEndpoint, String region, Integer port, boolean iamAuth) {
        return iamAuth ? generateAuthToken(username, dbEndpoint, region, port) : DB_PASS;
    }

    private String generateAuthToken(String username, String dbEndpoint, String region, Integer port) {
        RdsUtilities utilities = RdsUtilities.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(region))
                .build();

        GenerateAuthenticationTokenRequest authTokenRequest = GenerateAuthenticationTokenRequest.builder()
                .username(username)
                .hostname(dbEndpoint)
                .port(port)
                .build();

        return utilities.generateAuthenticationToken(authTokenRequest);
    }

    private static Integer retrievePort(String envVarName, Integer defaultPort) {
        Integer port = defaultPort;
        try {
            port = Integer.valueOf(System.getenv(envVarName));
        } catch (NumberFormatException nfe) {
            log.warn("DB_PORT is not in environment variables or not an integer");
        }
        return port;
    }
}

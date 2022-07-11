package com.example;

import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.example.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class CustomerLambdaHandlerIntegrationTest {

    private static StreamLambdaHandler handler;
    private static Context lambdaContext;

    @BeforeAll
    public static void setUp() {
        handler = new StreamLambdaHandler();
        lambdaContext = new MockLambdaContext();
    }

    @Test
    public void when_createCustomer_respondsWithCustomer() throws JsonProcessingException {
        Customer customer = new Customer(0, "Demo User", "demo@domain.com");
        ObjectMapper objectMapper = new ObjectMapper();

        InputStream requestStream = new AwsProxyRequestBuilder("/customer", HttpMethod.POST)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(customer))
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);

        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusCode());
        Customer customerResponse = assertDoesNotThrow(
                () -> objectMapper.readValue(response.getBody(), Customer.class));
        assertTrue(customerResponse.getId() > 1);
    }

    @Test
    public void when_findCustomerById_returnsCustomer() {
        ObjectMapper objectMapper = new ObjectMapper();

        int customerId = 1;
        InputStream requestStream = new AwsProxyRequestBuilder("/customer/" + Integer.toString(customerId),
                HttpMethod.GET).buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        Customer customerResponse = assertDoesNotThrow(
                () -> objectMapper.readValue(response.getBody(), Customer.class));
        assertTrue(customerResponse.getId() == customerId);
    }

    @Test
    public void when_findCustomerById_returnsNotFound() {
        int customerId = 123456789;
        InputStream requestStream = new AwsProxyRequestBuilder("/customer/" + Integer.toString(customerId),
                HttpMethod.GET).buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);

        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatusCode());
    }

    private void handle(InputStream is, ByteArrayOutputStream os) {
        try {
            handler.handleRequest(is, os, lambdaContext);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private AwsProxyResponse readResponse(ByteArrayOutputStream responseStream) {
        try {
            return LambdaContainerHandler.getObjectMapper().readValue(responseStream.toByteArray(),
                    AwsProxyResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Error while parsing response: " + e.getMessage());
        }
        return null;
    }
}

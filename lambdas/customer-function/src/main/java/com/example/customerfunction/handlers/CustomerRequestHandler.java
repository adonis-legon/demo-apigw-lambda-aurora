package com.example.customerfunction.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.customerfunction.business.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

public abstract class CustomerRequestHandler {
    @Getter
    protected CustomerService customerService;

    @Getter
    protected ObjectMapper mapper;

    public CustomerRequestHandler() {
        this(new CustomerService());
    }

    public CustomerRequestHandler(CustomerService customerService) {
        this.customerService = customerService;
        mapper = new ObjectMapper();
    }

    public abstract APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context);
}

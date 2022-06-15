package com.example.customerfunction.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.customerfunction.business.CustomerService;
import com.example.customerfunction.exceptions.InvalidCustomerInputException;
import com.example.customerfunction.model.Customer;

import lombok.extern.slf4j.Slf4j;
import software.amazon.lambda.powertools.logging.Logging;
import software.amazon.lambda.powertools.tracing.Tracing;

@Slf4j
public class CreateCustomerHandler extends CustomerRequestHandler{

    public CreateCustomerHandler() {
        super();
    }

    public CreateCustomerHandler(CustomerService customerService) {
        super(customerService);
    }

    @Logging
    @Tracing
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
        String customerCreatedErrorStr = "";

        try {
            Customer customer = this.mapper.readValue(request.getBody(), Customer.class);

            validateInput(customer);
            customerService.add(customer);
            String customerCreatedStr = mapper.writeValueAsString(customer);

            apiGatewayProxyResponseEvent.withStatusCode(201).withBody(customerCreatedStr);
            log.info("Customer created: " + customerCreatedStr);
        } catch (InvalidCustomerInputException invalidCustomerInput) {
            customerCreatedErrorStr = "{\"error\":\"Error validating Customer input\", \"message\":\""
                    + invalidCustomerInput.getMessage() + "\"}";

            log.error(customerCreatedErrorStr);
            apiGatewayProxyResponseEvent.withStatusCode(400).withBody(customerCreatedErrorStr);
        } catch (Exception e) {
            customerCreatedErrorStr = "{\"error\":\"Server error when processing event from AWS\", \"message\":\""
                    + e.getMessage() + "\"}";

            log.error(customerCreatedErrorStr);
            apiGatewayProxyResponseEvent.withStatusCode(500).withBody(customerCreatedErrorStr);
        }

        return apiGatewayProxyResponseEvent;
    }
    
    private void validateInput(Customer customer) throws InvalidCustomerInputException {
        if (customer == null || customer.getName() == null || customer.getName().isEmpty()
                || customer.getEmail() == null || customer.getEmail().isEmpty()) {
            throw new InvalidCustomerInputException(customer);
        }
    }
}

package com.example.customerfunction.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.customerfunction.business.CustomerService;
import com.example.customerfunction.exceptions.InvalidCustomerInputException;
import com.example.customerfunction.model.Customer;

import software.amazon.lambda.powertools.tracing.Tracing;

public class CreateCustomerHandler extends CustomerRequestHandler{

    public CreateCustomerHandler() {
        super();
    }

    public CreateCustomerHandler(CustomerService customerService) {
        super(customerService);
    }

    @Tracing
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
        LambdaLogger logger = context.getLogger();
        
        String customerCreatedErrorStr = "";
        try {
            Customer customer = this.mapper.readValue(request.getBody(), Customer.class);

            validateInput(customer);
            customerService.add(customer);
            String customerCreatedStr = mapper.writeValueAsString(customer);

            apiGatewayProxyResponseEvent.withStatusCode(201).withBody(customerCreatedStr);
            logger.log("Customer created: " + customerCreatedStr);
        } catch (InvalidCustomerInputException invalidCustomerInput) {
            customerCreatedErrorStr = "{\"error\":\"Error validating Customer input\", \"message\":\""
                    + invalidCustomerInput.getMessage() + "\"}";

            logger.log(customerCreatedErrorStr);
            apiGatewayProxyResponseEvent.withStatusCode(400).withBody(customerCreatedErrorStr);
        } catch (Exception e) {
            customerCreatedErrorStr = "{\"error\":\"Server error when processing event from AWS\", \"message\":\""
                    + e.getMessage() + "\"}";

            logger.log(customerCreatedErrorStr);
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

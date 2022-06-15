package com.example.customerfunction.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.customerfunction.business.CustomerService;
import com.example.customerfunction.exceptions.NotFoundCustomerException;
import com.example.customerfunction.model.Customer;

import lombok.extern.slf4j.Slf4j;
import software.amazon.lambda.powertools.logging.Logging;
import software.amazon.lambda.powertools.tracing.Tracing;

@Slf4j
public class FindCustomerByIdHandler extends CustomerRequestHandler{

    public FindCustomerByIdHandler() {
        super();
    }

    public FindCustomerByIdHandler(CustomerService customerService) {
        super(customerService);
    }

    @Logging
    @Tracing
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
        String customerCreatedErrorStr = "";
        String customerId = request.getPathParameters().get("id");

        try {
            Customer customer = customerService.findById(Integer.parseInt(customerId));
            if(customer == null){
                throw new NotFoundCustomerException(customerId);
            }
            
            String customerCreatedStr = mapper.writeValueAsString(customer);

            apiGatewayProxyResponseEvent.withStatusCode(200).withBody(customerCreatedStr);
            log.info("Customer created: " + customerCreatedStr);
        }catch(NumberFormatException numberEx){
            customerCreatedErrorStr = "{\"error\":\"Server error when processing event from AWS\", \"message\":\"Invalid customer id: " + customerId + "\"}";

            log.error(customerCreatedErrorStr);
            apiGatewayProxyResponseEvent.withStatusCode(400).withBody(customerCreatedErrorStr);
        } 
        catch(NotFoundCustomerException notFoundEx){
            customerCreatedErrorStr = "{\"error\":\"Server error when processing event from AWS\", \"message\":\"" + notFoundEx.getMessage() + "\"}";

            log.error(customerCreatedErrorStr);
            apiGatewayProxyResponseEvent.withStatusCode(404).withBody(customerCreatedErrorStr);
        }
        catch (Exception e) {
            customerCreatedErrorStr = "{\"error\":\"Server error when processing event from AWS\", \"message\":\"" + e.getMessage() + "\"}";

            log.error(customerCreatedErrorStr);
            apiGatewayProxyResponseEvent.withStatusCode(500).withBody(customerCreatedErrorStr);
        }

        return apiGatewayProxyResponseEvent;
    }
    
}

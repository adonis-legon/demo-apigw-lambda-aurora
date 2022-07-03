package com.example.customerfunction.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.customerfunction.business.CustomerService;
import com.example.customerfunction.exceptions.NotFoundCustomerException;
import com.example.customerfunction.model.Customer;

import software.amazon.lambda.powertools.tracing.Tracing;

public class FindCustomerByIdHandler extends CustomerRequestHandler{

    public FindCustomerByIdHandler() {
        super();
    }

    public FindCustomerByIdHandler(CustomerService customerService) {
        super(customerService);
    }

    @Tracing
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
        LambdaLogger logger = context.getLogger();
        String customerCreatedErrorStr = "";
        String customerId = request.getPathParameters().get("id");

        try {
            Customer customer = customerService.findById(Integer.parseInt(customerId));
            if(customer == null){
                throw new NotFoundCustomerException(customerId);
            }
            
            String customerCreatedStr = mapper.writeValueAsString(customer);

            apiGatewayProxyResponseEvent.withStatusCode(200).withBody(customerCreatedStr);
            logger.log("Customer found: " + customerCreatedStr);
        }catch(NumberFormatException numberEx){
            customerCreatedErrorStr = "{\"error\":\"Server error when processing event from AWS\", \"message\":\"Invalid customer id: " + customerId + "\"}";

            logger.log(customerCreatedErrorStr);
            apiGatewayProxyResponseEvent.withStatusCode(400).withBody(customerCreatedErrorStr);
        } 
        catch(NotFoundCustomerException notFoundEx){
            customerCreatedErrorStr = "{\"error\":\"Server error when processing event from AWS\", \"message\":\"" + notFoundEx.getMessage() + "\"}";

            logger.log(customerCreatedErrorStr);
            apiGatewayProxyResponseEvent.withStatusCode(404).withBody(customerCreatedErrorStr);
        }
        catch (Exception e) {
            customerCreatedErrorStr = "{\"error\":\"Server error when processing event from AWS\", \"message\":\"" + e.getMessage() + "\"}";

            logger.log(customerCreatedErrorStr);
            apiGatewayProxyResponseEvent.withStatusCode(500).withBody(customerCreatedErrorStr);
        }

        return apiGatewayProxyResponseEvent;
    }
    
}

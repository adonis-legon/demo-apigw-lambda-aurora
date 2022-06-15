package com.example.customerfunction.functions;

import org.apache.http.HttpStatus;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.customerfunction.handlers.CreateCustomerHandler;
import com.example.customerfunction.handlers.FindCustomerByIdHandler;
import com.example.customerfunction.business.CustomerService;
import com.example.customerfunction.exceptions.InvalidCustomerMethodException;
import com.example.customerfunction.handlers.CustomerRequestHandler;

public class CustomerFunction implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final String HTTP_GET = "GET";
    private final String HTTP_POST = "POST";

    private CustomerService customerService;

    public CustomerFunction() {
        this.customerService = new CustomerService();
    }

    public CustomerFunction(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            return buildCustomerRequestHandler(input).handleRequest(input, context);
        } catch (InvalidCustomerMethodException invalidMethodEx) {
            return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatus.SC_NOT_IMPLEMENTED)
                    .withBody(invalidMethodEx.getMessage());
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .withBody(e.getMessage());
        }

    }

    private CustomerRequestHandler buildCustomerRequestHandler(APIGatewayProxyRequestEvent request)
            throws InvalidCustomerMethodException {
        switch (request.getHttpMethod()) {
            case HTTP_POST:
                return new CreateCustomerHandler(this.customerService);
            case HTTP_GET:
                return new FindCustomerByIdHandler(this.customerService);
            default:
                throw new InvalidCustomerMethodException(request.getHttpMethod());
        }
    }
}

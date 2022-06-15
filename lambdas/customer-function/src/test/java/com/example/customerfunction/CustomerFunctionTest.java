package com.example.customerfunction;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.amazonaws.services.lambda.runtime.tests.annotations.HandlerParams;
import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.core.JsonProcessingException;
import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.annotations.Events;
import com.amazonaws.services.lambda.runtime.tests.annotations.Responses;
import com.example.customerfunction.business.CustomerService;
import com.example.customerfunction.exceptions.FindCustomerException;
import com.example.customerfunction.functions.CustomerFunction;
import com.example.customerfunction.model.Customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

import org.json.JSONException;

@ExtendWith(MockitoExtension.class)
public class CustomerFunctionTest {

    @Mock
    private Context context;

    // @Mock
    private CustomerService customerService;

    private ObjectMapper mapper;

    public CustomerFunctionTest() {
        customerService = new CustomerService();
        mapper = new ObjectMapper();
    }

    @ParameterizedTest
    @HandlerParams(
            events = @Events(folder = "create-customer/events/", type = APIGatewayProxyRequestEvent.class),
            responses = @Responses(folder = "create-customer/responses/", type = APIGatewayProxyResponseEvent.class)
    )
    public void testCreateCustomer(APIGatewayProxyRequestEvent event, APIGatewayProxyResponseEvent response) {
        APIGatewayProxyResponseEvent result = new CustomerFunction(this.customerService).handleRequest(event, context);

        assertThat(result.getStatusCode()).isEqualTo(response.getStatusCode());
    }

    @ParameterizedTest
    @HandlerParams(
            events = @Events(folder = "find-customer-by-id/events/", type = APIGatewayProxyRequestEvent.class),
            responses = @Responses(folder = "find-customer-by-id/responses/", type = APIGatewayProxyResponseEvent.class)
    )
    public void testFindCustomerById(APIGatewayProxyRequestEvent event, APIGatewayProxyResponseEvent response) 
    throws JsonProcessingException, JSONException, FindCustomerException {
        int customerId = 1;
        Customer customer = new Customer(customerId, "Demo", "demo@domain.com");

        lenient().when(customerService.findById(customerId)).thenReturn(customer);

        APIGatewayProxyResponseEvent result = new CustomerFunction(this.customerService).handleRequest(event, context);

        assertThat(result.getStatusCode()).isEqualTo(response.getStatusCode());

        if(response.getStatusCode() == 200){
            JSONAssert.assertEquals(mapper.writeValueAsString(customer), response.getBody(), JSONCompareMode.LENIENT);
        }
    }
}

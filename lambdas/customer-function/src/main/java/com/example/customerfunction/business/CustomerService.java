package com.example.customerfunction.business;

import java.sql.SQLException;

import com.example.customerfunction.dao.CustomerDAO;
import com.example.customerfunction.exceptions.CreateCustomerException;
import com.example.customerfunction.exceptions.FindCustomerException;
import com.example.customerfunction.model.Customer;

import software.amazon.lambda.powertools.tracing.Tracing;
import software.amazon.lambda.powertools.tracing.TracingUtils;

public class CustomerService {
    private CustomerDAO customerDAO;

    public CustomerService() {
        customerDAO = new CustomerDAO();
    }

    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Tracing
    public void add(Customer customer) throws CreateCustomerException{
        try {
            customerDAO.save(customer);

            // adding custom annotation and metadata for tracing of business transaction
            addCustomTracingInfo(customer);
        } catch (SQLException e) {
            throw new CreateCustomerException(e);
        }
    }

    @Tracing
    public Customer findById(int customerId) throws FindCustomerException{
        try {
            Customer customer = customerDAO.findById(customerId);

            // adding custom annotation and metadata for tracing of business transaction
            if(customer != null){
                addCustomTracingInfo(customer);
            }

            return customer;
        } catch (SQLException e) {
            throw new FindCustomerException(e);
        }
    }

    private void addCustomTracingInfo(Customer customer){
        TracingUtils.putAnnotation("customerId", Integer.toString(customer.getId()));
        TracingUtils.putMetadata("resources", "customer", customer);
    }
}
package com.example.business;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.CustomerDAO;
import com.example.exceptions.CreateCustomerException;
import com.example.exceptions.FindCustomerException;
import com.example.exceptions.NotFoundCustomerException;
import com.example.model.Customer;

import software.amazon.lambda.powertools.tracing.Tracing;
import software.amazon.lambda.powertools.tracing.TracingUtils;

@Service
public class CustomerService {

    @Autowired
    private CustomerDAO customerDAO;

    @Tracing
    public void add(Customer customer) throws CreateCustomerException {
        try {
            customerDAO.save(customer);

            // adding custom annotation and metadata for tracing of business transaction
            addCustomTracingInfo(customer);
        } catch (SQLException e) {
            throw new CreateCustomerException(e);
        }
    }

    @Tracing
    public Customer findById(int customerId) throws FindCustomerException, NotFoundCustomerException {
        try {
            Customer customer = customerDAO.findById(customerId);

            // adding custom annotation and metadata for tracing of business transaction
            if (customer != null) {
                addCustomTracingInfo(customer);
            } else {
                throw new NotFoundCustomerException(Integer.toString(customerId));
            }

            return customer;
        } catch (NotFoundCustomerException notFoundEx) {
            throw notFoundEx;
        } catch (Exception e) {
            throw new FindCustomerException(e);
        }
    }

    private void addCustomTracingInfo(Customer customer) {
        TracingUtils.putAnnotation("customerId", Integer.toString(customer.getId()));
        TracingUtils.putMetadata("resources", "customer", customer);
    }
}
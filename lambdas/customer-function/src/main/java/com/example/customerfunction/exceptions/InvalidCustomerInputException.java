package com.example.customerfunction.exceptions;

import com.example.customerfunction.model.Customer;

public class InvalidCustomerInputException extends Exception{
    public InvalidCustomerInputException(Customer customer) {
        super("Error processing customer input, for customer: " + customer.getId());
    }
}

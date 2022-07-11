package com.example.exceptions;

public class NotFoundCustomerException extends Exception{
    public NotFoundCustomerException(String customerId) {
        super("Error searching customer with id: " + customerId);
    }
}

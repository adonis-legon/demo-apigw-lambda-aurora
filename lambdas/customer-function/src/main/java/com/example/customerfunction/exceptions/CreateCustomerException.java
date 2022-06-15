package com.example.customerfunction.exceptions;

public class CreateCustomerException extends Exception{
    public CreateCustomerException(Exception reason) {
        super("Error creating customer.", reason);
    }
}

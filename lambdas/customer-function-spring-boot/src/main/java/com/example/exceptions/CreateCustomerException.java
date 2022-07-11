package com.example.exceptions;

public class CreateCustomerException extends Exception{
    public CreateCustomerException(Exception reason) {
        super("Error creating customer.", reason);
    }
}

package com.example.customerfunction.exceptions;

public class InvalidCustomerMethodException extends Exception{
    public InvalidCustomerMethodException(String httpMethod) {
        super("Invalid Customer method: " + httpMethod);
    }
}

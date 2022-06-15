package com.example.customerfunction.exceptions;

public class FindCustomerException extends Exception{
    public FindCustomerException(Exception reason) {
        super("Error searching customer", reason);
    }
}

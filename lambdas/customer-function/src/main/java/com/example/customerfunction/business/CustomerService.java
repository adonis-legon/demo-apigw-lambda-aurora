package com.example.customerfunction.business;

import java.sql.SQLException;

import com.example.customerfunction.dao.CustomerDAO;
import com.example.customerfunction.exceptions.CreateCustomerException;
import com.example.customerfunction.exceptions.FindCustomerException;
import com.example.customerfunction.model.Customer;

public class CustomerService {
    private CustomerDAO customerDAO;

    public CustomerService() {
        customerDAO = new CustomerDAO();
    }

    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public Customer findById(int customerId) throws FindCustomerException{
        try {
            return customerDAO.findById(customerId);
        } catch (SQLException e) {
            throw new FindCustomerException(e);
        }
    }

    public void add(Customer customer) throws CreateCustomerException{
        try {
            customerDAO.save(customer);
        } catch (SQLException e) {
            throw new CreateCustomerException(e);
        }
    }
}

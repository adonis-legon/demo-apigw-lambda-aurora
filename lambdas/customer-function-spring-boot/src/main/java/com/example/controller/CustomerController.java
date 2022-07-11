package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.business.CustomerService;
import com.example.exceptions.CreateCustomerException;
import com.example.exceptions.FindCustomerException;
import com.example.exceptions.NotFoundCustomerException;
import com.example.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@EnableWebMvc
@RequestMapping("/customer")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    @Qualifier("mapper")
    private ObjectMapper mapper; 

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
        try {
            customerService.add(customer);
            log.info("Customer created: " + mapper.writeValueAsString(customer));

            return ResponseEntity.status(HttpStatus.CREATED).body(customer);
        } catch (CreateCustomerException | JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping(value = "/{customerId}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable int customerId){
        try {
            Customer customer = customerService.findById(customerId);
            log.info("Customer found: " + mapper.writeValueAsString(customer));

            return ResponseEntity.status(HttpStatus.OK).body(customer);
        } catch (NotFoundCustomerException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (FindCustomerException | JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}

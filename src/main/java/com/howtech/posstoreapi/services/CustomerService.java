package com.howtech.posstoreapi.services;

import com.howtech.posstoreapi.DTOs.CustomerDto;
import com.howtech.posstoreapi.clients.CustomerClient;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerClient customerClient;

    public CustomerService(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }

    public CustomerDto getById(Long customerId) {
        return customerClient.getById(customerId);
    }
}

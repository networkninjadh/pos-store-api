package com.howtech.posstoreapi.clients;

import com.howtech.posstoreapi.DTOs.CustomerDto;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomerClient {

    private final RestTemplate restTemplate;

    public CustomerClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    String URL = "http://localhost:8082";
    
    public CustomerDto getById(Long customerId) {

        String CUSTOMER = "/customer-api/customer/";
        ResponseEntity<CustomerDto> response = restTemplate
                .getForEntity(URL + CUSTOMER + customerId, CustomerDto.class);
        return response.getBody();
    }

}

package com.howtech.posstoreapi.clients;

import com.howtech.posstoreapi.DTOs.CustomerDto;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomerClient {

    private RestTemplate restTemplate;

    private String URL = "http://localhost:8082";
    private String CUSTOMER = "/customer-api/customer/";

    public CustomerDto getById(Long customerId) {
        ResponseEntity<CustomerDto> response = restTemplate
                .getForEntity(URL + CUSTOMER + customerId, CustomerDto.class);
        return response.getBody();
    }

}

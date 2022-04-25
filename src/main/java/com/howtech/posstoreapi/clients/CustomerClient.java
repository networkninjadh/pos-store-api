package com.howtech.posstoreapi.clients;

import com.howtech.posstoreapi.DTOs.CustomerDto;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomerClient {

    private final RestTemplate restTemplate;

    String URL = "http://localhost:8082";

    public CustomerClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CustomerDto getById(Long customerId) {

        String CUSTOMER = URL + "/customer-api/customer/" + customerId;

        ResponseEntity<CustomerDto> response = restTemplate
                .getForEntity(CUSTOMER, CustomerDto.class);

        return response.getBody();
    }

    public CustomerDto[] getAllCustomers() {

        String CUSTOMERS = URL + "/customer-api/customers";

        ResponseEntity<CustomerDto[]> response = restTemplate
                .getForEntity(CUSTOMERS, CustomerDto[].class);

        return response.getBody();
    }
}

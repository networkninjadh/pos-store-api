package com.howtech.posstoreapi.clients;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.howtech.posstoreapi.DTOs.DriverDto;

@Component
public class DriverClient {

    private final RestTemplate restTemplate;

    private final String URL = "http://localhost:8083";

    public DriverClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DriverDto getById(Long driverId) {

        String DRIVER = URL + "/driver-api/driver/" + driverId;

        ResponseEntity<DriverDto> response = restTemplate
                .getForEntity(DRIVER, DriverDto.class);

        return response.getBody();
    }

    public DriverDto[] getAllDrivers() {

        String DRIVERS = URL + "/driver-api/drivers";

        ResponseEntity<DriverDto[]> response = restTemplate
                .getForEntity(DRIVERS, DriverDto[].class);

        return response.getBody();
    }
}

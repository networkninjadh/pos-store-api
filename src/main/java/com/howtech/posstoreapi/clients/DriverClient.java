package com.howtech.posstoreapi.clients;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.howtech.posstoreapi.DTOs.DriverDto;

import java.util.List;

@Component
public class DriverClient {

    private final RestTemplate restTemplate;

    private final String URL = "http://localhost:8083";

    public DriverClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DriverDto getById(Long driverId) {

        String DRIVER = "/driver-api/driver";

        ResponseEntity<DriverDto> response = restTemplate
                .getForEntity(URL + driverId, DriverDto.class);

        return response.getBody();
    }

    public DriverDto[] getAllDrivers() {

        String DRIVERS = "/driver-api/drivers";

        ResponseEntity<DriverDto[]> response = restTemplate
                .getForEntity(URL, DriverDto[].class);

        return response.getBody();
    }
}

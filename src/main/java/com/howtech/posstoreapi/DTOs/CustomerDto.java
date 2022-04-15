package com.howtech.posstoreapi.DTOs;

import lombok.Data;

/**
 *
 * @author Damond Howard
 * @apiNote This is a DTO object for creating a new customer in the database
 *
 */
@Data
public class CustomerDto {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String addressCountry;
    private String addressStreet;
    private String addressCity;
    private String addressProvince;
    private String addressPostcode;
}
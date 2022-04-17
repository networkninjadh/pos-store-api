package com.howtech.posstoreapi.DTOs;
import lombok.Data;

@Data
public class DriverDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String driverProfileImg;
    private int completedTrips = 0;
    private int ratings = 0;
    private int rating = 0;
    private String phoneNumber;
    private boolean online = false;
    private String make;
    private String model;
    private String vehicleColor;
    private String licensePlate;
    private Long driversLicense;
    private boolean approvedToDrive = false;
}

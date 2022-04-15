package com.howtech.posstoreapi.controllers;

import com.howtech.posstoreapi.DTOs.CustomerDto;
import com.howtech.posstoreapi.DTOs.CustomerOrder;
import com.howtech.posstoreapi.DTOs.UserInfo;
import com.howtech.posstoreapi.models.store.Employee;
import com.howtech.posstoreapi.models.store.Product;
import com.howtech.posstoreapi.models.store.Store;
import com.howtech.posstoreapi.models.store.StoreOrder;
import com.howtech.posstoreapi.services.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.sql.Driver;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Damond Howard
 * @apiNote This controller returns all info necessary for a store owner that is
 *          logged in used for store administrative panel
 *
 */

@RestController
@RequestMapping(path = "/store-owner-api")
public class StoreOwnerController {

    Logger LOGGER = LoggerFactory.getLogger(StoreOwnerController.class);

    private final StoreService storeService;

    public StoreOwnerController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/my-stores")
    public List<Store> getStores(UserInfo userInfo) {
        return storeService.getByOwnerName(userInfo);
    }

    @GetMapping("/my-employees")
    public Set<Employee> getEmployees(UserInfo userInfo) {
        return storeService.getEmployees(userInfo);
    }

    @GetMapping("/my-inventory")
    public Set<Product> getInventory(UserInfo userInfo) {
        return storeService.getInventory(userInfo);
    }

    @GetMapping("/my-orders")
    public Set<StoreOrder> getOrders(UserInfo userInfo) {
        return storeService.getOrders(userInfo);
    }

    @GetMapping("/my-customers")
    public Set<CustomerDto> getCustomers(UserInfo userInfo) {
        return storeService.getCustomers(userInfo);
    }

    @GetMapping("/my-drivers")
    public Set<Driver> getDrivers(@AuthenticationPrincipal UserDetails userDetails) {
        // TODO get all drivers who have picked up from the store
        // TODO has to be done after writing driver controller
        return null;
    }

    @GetMapping("/my-shipments")
    public Set<CustomerOrder> getShipments(@AuthenticationPrincipal UserDetails userDetails) {
        return null; // TODO finish once the relay system for customers and orders is finished
    }
}
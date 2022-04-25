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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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

    /**
     * 
     * @param username
     * @return
     */
    @GetMapping("/my-stores")
    public List<Store> getStores(@RequestHeader(name = "user-token") String username) {
        return storeService.getByOwnerName(username);
    }

    /**
     * 
     * @param username
     * @return
     */
    @GetMapping("/my-employees")
    public Set<Employee> getEmployees(@RequestHeader(name = "user-token") String username) {
        return storeService.getEmployees(username);
    }

    /**
     * @param username the username from pos-auth-api
     */
    @GetMapping("/my-inventory")
    public Set<Product> getInventory(@RequestHeader(name = "user-token") String username) {
        return storeService.getInventory(username);
    }

    /**
     * 
     * @param username
     * @return
     */
    @GetMapping("/my-orders")
    public Set<StoreOrder> getOrders(@RequestHeader(name = "user-token") String username) {
        return storeService.getOrders(username);
    }

    /**
     * 
     * @param username
     * @return
     */
    @GetMapping("/my-customers")
    public Set<CustomerDto> getCustomers(@RequestHeader(name = "user-token") String username) {
        return storeService.getCustomers(username);
    }

    /**
     * 
     * @param username
     * @return
     */
    @GetMapping("/my-drivers")
    public Set<Driver> getDrivers(@RequestHeader(name = "user-token") String username) {
        // TODO get all drivers who have picked up from the store
        // TODO has to be done after writing driver controller
        return null;
    }

    /**
     * @param username the username from pos-auth-api
     */
    @GetMapping("/my-shipments")
    public Set<CustomerOrder> getShipments(@RequestHeader(name = "user-token") String username) {
        return null; // TODO finish once the relay system for customers and orders is finished
    }
}
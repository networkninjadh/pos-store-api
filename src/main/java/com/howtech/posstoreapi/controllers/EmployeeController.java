package com.howtech.posstoreapi.controllers;

import java.util.Set;

import com.howtech.posstoreapi.DTOs.UserInfo;
import com.howtech.posstoreapi.exceptions.EmployeeNotFoundException;
import com.howtech.posstoreapi.exceptions.StoreNotFoundException;
import com.howtech.posstoreapi.models.store.Employee;
import com.howtech.posstoreapi.models.store.Store;
import com.howtech.posstoreapi.services.StoreService;

import org.apache.catalina.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Damond Howard
 * @apiNote This is a restfull api for handling all interactions with employees
 *          through a store
 *
 */
@RestController
@RequestMapping(path = "/employee-api/")
public class EmployeeController {

    private final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    private final StoreService storeService;

    public EmployeeController(StoreService storeService) {
        this.storeService = storeService;
    }

    /**
     *
     * @param newEmployeeData data for new employee
     * @param userInfo user's information from pos-auth-api
     * @param storeId the ID of the store in which the new employee will be added to
     * @return the Store that the current employee has been saved to
     * @throws StoreNotFoundException if there is no store with ID storeId found in the database
     */
    @PostMapping("/store/{store_id}/employee/new")
    public Store addEmployee(@RequestBody Employee newEmployeeData, UserInfo userInfo,
                             @PathVariable(name = "store_id") Long storeId) throws StoreNotFoundException {
        LOGGER.info("Employee Controller -- Creating new employee for store with id " + storeId
                + " /store/{store_id}/employee/new");
        return storeService.addEmployee(storeId, newEmployeeData, userInfo);
    }

    /**
     *
     * @param storeId the ID of the store which the employee belongs to
     * @param employeeId the ID of the employee to find
     * @return the Employee object that was requested by id
     * @throws EmployeeNotFoundException if No employee with ID employeeId is found in the database
     * @throws StoreNotFoundException if No store with ID storeId is found in the database
     */
    @GetMapping("/store/{store_id}/employee/{employee_id}")
    public Employee getEmployee(@PathVariable(name = "store_id") Long storeId,
                                @PathVariable(name = "employee_id") Long employeeId, UserInfo userInfo)
            throws EmployeeNotFoundException, StoreNotFoundException {
        return storeService.getEmployee(storeId, employeeId, userInfo);
    }

    @PutMapping("/store/{store_id}/employee/{employee_id}")
    public Store changeEmployeeData(@PathVariable(name = "store_id") Long storeId,
                                    @PathVariable(name = "employee_id") Long employeeId, @RequestBody Employee newEmployeeData,
                                    UserInfo userInfo) throws StoreNotFoundException {
        return storeService.changeEmployeeData(storeId, employeeId, newEmployeeData, userInfo);
    }

    /**
     *
     * @param storeId the ID of the store in which the employee belongs
     * @return all employees from store with ID storeId
     * @throws StoreNotFoundException if No store with ID storeId is found in the database
     */
    @GetMapping("/store/{store_id}/employee/all")
    public Set<Employee> getEmployees(@PathVariable(name = "store_id") Long storeId,
                                      UserInfo userInfo) throws StoreNotFoundException {
        return storeService.getEmployees(storeId, userInfo);
    }

    /**
     *
     * @param storeId the ID of the store which the employee belongs to
     * @param employeeId the ID of the employee
     * @return the new store object minus the employee that has been deleted
     * @throws StoreNotFoundException if No store with ID storeId is found in the database
     */
    @DeleteMapping("/store/{store_id}/delete/{employee_id}")
    public Store deleteEmployee(UserInfo userInfo,
                                @PathVariable(name = "store_id") Long storeId, @PathVariable Long employeeId)
            throws StoreNotFoundException {
        return storeService.deleteEmployee(storeId, employeeId, userInfo);
    }

    /**
     *
     * @param storeId the ID of the store which the employee belongs
     * @return the new store with all the employees deleted out of the database
     * @throws StoreNotFoundException if No store with ID storeId is found in the database
     */
    @DeleteMapping("/store/{store_id}/delete/all")
    public Store deleteEmployees(UserInfo userInfo,
                                 @PathVariable(name = "store_id") Long storeId) throws StoreNotFoundException {
        return storeService.deleteEmployees(storeId, userInfo);
    }
}

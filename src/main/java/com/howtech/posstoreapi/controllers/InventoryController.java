package com.howtech.posstoreapi.controllers;

import java.util.Set;

import com.howtech.posstoreapi.DTOs.UserInfo;
import com.howtech.posstoreapi.exceptions.StoreNotFoundException;
import com.howtech.posstoreapi.models.store.Product;
import com.howtech.posstoreapi.models.store.Store;
import com.howtech.posstoreapi.services.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @author Damond Howard
 * @apiNote this is a restfull controller that manages the inventory of each
 *          store
 */
@RestController
@RequestMapping(path = "/inventory-api/")
public class InventoryController {

    Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);

    private final StoreService storeService;

    public InventoryController(StoreService storeService) {
        this.storeService = storeService;
    }

    /**
     * @param storeId the ID of the store which holds the inventory
     * @param product product information
     * @param username the username from pos-auth-api
     * @return the new product that was added to the stores inventory
     * @throws StoreNotFoundException if No store with ID storeId if found in the database
     */
    @PostMapping("/store/{store_id}/inventory/new")
    public Set<Product> addItem(@PathVariable(name = "store_id") Long storeId,
                                @RequestBody Product product,
                                @RequestHeader(name = "user-token") String username
                                ) throws StoreNotFoundException {
        return storeService.addInventoryItem(storeId, product, username);
    }

    /**
     *
     * @param storeId the ID of the store
     * @param inventoryId the ID of the InventoryItem in a store's inventory
     * @param username the username from pos-auth-api
     * @return a new Product
     * @throws StoreNotFoundException if No store with ID storeId is found in the database
     */
    @GetMapping("/store/{store_id}/inventory/{inventory_id}")
    public Product getIventoryItem(@PathVariable(name = "store_id") Long storeId,
                                   @PathVariable(name = "inventory_id") Long inventoryId, 
                                   @RequestHeader(name = "user-token") String username)
            throws StoreNotFoundException {
        return storeService.getInventoryItem(storeId, inventoryId, username);
    }

    /**
     *
     * @param storeId the ID of the store
     * @param inventoryId the ID of the InventoryItem in a store's inventory
     * @param product information about a product
     * @param username the username from pos-auth-api
     * @return the new Store after the inventory has been modified
     * @throws StoreNotFoundException if No store with ID storeId is found in the database
     */
    @PutMapping("/store/{store_id}/inventory/{inventory_id}")
    public Store changeInventoryItem(@PathVariable(name = "store_id") Long storeId,
                                     @PathVariable(name = "inventory_id") Long inventoryId, @RequestBody Product product,
                                     @RequestHeader(name = "user-token") String username
                                     ) throws StoreNotFoundException {
        return storeService.changeInventoryItem(storeId, inventoryId, product, username);
    }

    /**
     *
     * @param storeId the ID of the store
     * @param username the username from pos-auth-api
     * @return the list of all inventory in a certain store
     * @throws StoreNotFoundException if No store with ID storeId is found in the database
     */
    @GetMapping("/store/{store_id}/inventory")
    public Set<Product> getInventory(@PathVariable(name = "store_id") Long storeId,
                                     @RequestHeader(name = "user-token") String username
                                    ) throws StoreNotFoundException {
        return storeService.getInventory(storeId, username);
    }

    /**
     *
     * @param storeId the ID of the store
     * @param inventoryId the ID of the InventoryItem in the store
     * @param username the username from pos-auth-api
     * @return a String message stating the field has been deleted
     * @throws StoreNotFoundException if No store with ID storeId is found in the database
     */
    @DeleteMapping("/store/{store_id}/inventory/{inventory_id}/delete")
    public String deleteInventoryItem(@PathVariable(name = "store_id") Long storeId,
                                      @PathVariable(name = "inventory_id") Long inventoryId, 
                                      @RequestHeader(name = "user-token") String username)
            throws StoreNotFoundException {
        return storeService.deleteInventoryItem(storeId, inventoryId, username);
    }

    /**
     *
     * @param storeId the ID of the store
     * @param userInfo the user's information from pos-auth-api
     * @return a String message confirming that the stores inventory has been
     *         deleted
     * @throws StoreNotFoundException if No store with ID storeId is found in the database
     */
    @DeleteMapping("/store/{store_id}/delete/all")
    public String deleteInventory(@PathVariable(name = "store_id") Long storeId,
                                  @RequestHeader(name = "user-token") String username) throws StoreNotFoundException {
        return storeService.deleteInventory(storeId, username);
    }
}
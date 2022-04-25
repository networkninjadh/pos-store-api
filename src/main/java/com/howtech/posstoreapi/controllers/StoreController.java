package com.howtech.posstoreapi.controllers;

import java.util.List;

import com.howtech.posstoreapi.DTOs.HoursDto;
import com.howtech.posstoreapi.DTOs.StoreDto;
import com.howtech.posstoreapi.DTOs.UserInfo;
import com.howtech.posstoreapi.exceptions.StoreNotFoundException;
import com.howtech.posstoreapi.models.store.Employee;
import com.howtech.posstoreapi.models.store.Store;
import com.howtech.posstoreapi.services.StoreService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * 
 * @author Damond Howard
 * @apiNote Restful Controller that encompasses all of the endpoints associated
 *          with updating a users stores
 */
@RestController
@RequestMapping(path = "/store-api/")
public class StoreController {

	Logger LOGGER = LoggerFactory.getLogger(StoreController.class);

	private final StoreService storeService;

	public StoreController(StoreService storeService) {
		this.storeService = storeService;
	}

	/**
	 * 
	 * @param storeId  the store to set the hours for
	 * @param hours    an object containing all the openening and closing hours for a store including special holidays
	 * @param username the username from pos-auth-api
	 * @return the new store with the hours added
	 * @throws StoreNotFoundException
	 */
	@PostMapping("/store/{store_id}/hours")
	public Store addHours(@PathVariable(name = "store_id") Long storeId, 
						  @RequestBody HoursDto hours,
			              @RequestHeader(name = "user-token") String username
						 ) throws StoreNotFoundException {
		return storeService.addStoreHours(storeId, username, hours);
	}

	/**
	 * 
	 * @param store requestBody contains a Store object as JSON
	 * @poaram username the username from pos-auth-api
	 * @return the store object if it is persisted to the database
	 * @throws Exception
	 */
	@PostMapping("/store/new")
	public Store addStore(@RequestBody StoreDto store, 
						  @RequestHeader(name = "user-token", required = true) String username
						 ) {
		LOGGER.info("Creating a new Store for user " + username);
		return storeService.createFromDto(store, username);
	}

	/**
	 * 
	 * @param storeId
	 * @param userInfo
	 * @return a confirmation message confirming that a store has been opened
	 * @throws StoreNotFoundException
	 */
	@PostMapping("/store/{store_id}/open")
	public String openStore(@PathVariable(name = "store_id") Long storeId,
							@RequestHeader(name = "user-token") String username,
							@RequestBody Employee employee)
							throws StoreNotFoundException {
		return storeService.openStore(storeId, username, employee);
	}

	/**
	 * 
	 * @param storeId
	 * @param userInfo
	 * @return a confirmation message confirming that a store has been closed
	 * @throws StoreNotFoundException
	 */
	@PostMapping("/store/{store_id}/close")
	public String closeStore(@PathVariable(name = "store_id") Long storeId,
							 @RequestHeader(name = "user-token") String username
							) throws StoreNotFoundException {
		return storeService.closeStore(storeId, username);
	}

	/**
	 * 
	 * @param storeId1 the logged in store that is refering another store
	 * @param storeId2 the store that is being refered
	 * @param username Auth Token from pos-auth-api
	 * @return a string value stating that the refereal was successful
	 * @throws StoreNotFoundException
	 */
	@PostMapping("/store/{store_id_1}/refers/store/{store_id_2}")
	public String referAStore(@PathVariable(name = "store_id_1") Long storeId1,
							  @PathVariable(name = "store_id_2") Long storeId2,
							  @RequestHeader(name = "user-token") String username
							 ) throws StoreNotFoundException {
		return storeService.referAStore(storeId1, storeId2, username);

	}

	/**
	 * 
	 * @param storeId
	 * @param userInfo
	 * @return a list of all customer names in all stores owned by a certain user
	 */
	@GetMapping("/store/{store_id}/customers")
	public List<String> getCustomers(@PathVariable(name = "store_id") Long storeId,
			UserInfo userInfo) {
		return storeService.getCustomerList(storeId, userInfo);
	}

	/**
	 * 
	 * @param id
	 * @return the store based off the provided id
	 * @throws StoreNotFoundException
	 */
	@GetMapping("/store/{store_id}")
	public Store getStore(@PathVariable(name = "store_id") Long storeId) throws StoreNotFoundException {
		return storeService.getById(storeId);
	}

	/**
	 * 
	 * @return the list of all available stores in the repository
	 */
	@GetMapping("/stores")
	public List<Store> getStores(UserInfo userInfo) {
		return storeService.getAll();
	}

	/**
	 * 
	 * @param id identifier of Store to be deleted
	 * @return a string saying that the store has been deleted
	 */
	@DeleteMapping("/store/delete/{id}")
	public String deleteStore(@PathVariable Long id) {
		return storeService.delete(id);
	}

	/**
	 * 
	 * @return a confirmation message confirming that every store in the database
	 *         has been deleted
	 */
	@DeleteMapping("store/delete/all")
	public String deleteStores() {
		storeService.deleteStores();
		return "All stores have been deleted";
	}
}
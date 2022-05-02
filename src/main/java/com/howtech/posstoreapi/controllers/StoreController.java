package com.howtech.posstoreapi.controllers;

import java.io.IOException;
import java.util.List;

import com.howtech.posstoreapi.DTOs.HoursDto;
import com.howtech.posstoreapi.DTOs.StoreDto;
import com.howtech.posstoreapi.exceptions.StoreNotFoundException;
import com.howtech.posstoreapi.models.store.Employee;
import com.howtech.posstoreapi.models.store.Store;
import com.howtech.posstoreapi.services.StoreService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author Damond Howard
 * @apiNote Restful Controller that encompasses all the endpoints associated
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
	 * @param hours    an object containing all the opening and closing hours for a store including special holidays
	 * @param username the username from pos-auth-api
	 * @return the new store with the hours added
	 * @throws StoreNotFoundException if no Store with ID storeId is found
	 */
	@PostMapping("/store/{store_id}/hours")
	public Store addHours(@PathVariable(name = "store_id") Long storeId, 
						  @RequestBody HoursDto hours,
			              @RequestHeader(name = "user-token") String username
						 )
			throws StoreNotFoundException
	{
		return storeService.addStoreHours(storeId, username, hours);
	}

	/**
	 * 
	 * @param store requestBody contains a Store object as JSON
	 * @param username the username from pos-auth-api
	 * @return the store object if it is persisted to the database
	 */
	@PostMapping("/store/new")
	public Store addStore(@RequestBody StoreDto store, 
						  @RequestHeader(name = "user-token", required = true) String username
						 )
	{
		LOGGER.info("Creating a new Store for user " + username);
		return storeService.createFromDto(store, username);
	}

	/**
	 * 
	 * @param storeId store to set as open
	 * @param username username token from pos-auth-api
	 * @return a confirmation message confirming that a store has been opened
	 * @throws StoreNotFoundException if no Store with ID storeId is found in the database
	 */
	@PostMapping("/store/{store_id}/open")
	public String openStore(@PathVariable(name = "store_id") Long storeId,
							@RequestHeader(name = "user-token") String username,
							@RequestBody Employee employee
	                       )
			throws StoreNotFoundException {
		return storeService.openStore(storeId, username, employee);
	}

	/**
	 * 
	 * @param storeId the store to set as closed
	 * @param username username token from pos-auth-api
	 * @return a confirmation message confirming that a store has been closed
	 * @throws StoreNotFoundException if no Store with ID storeId is found in the database
	 */
	@PostMapping("/store/{store_id}/close")
	public String closeStore(@PathVariable(name = "store_id") Long storeId,
							 @RequestHeader(name = "user-token") String username
							)
			throws StoreNotFoundException {
		return storeService.closeStore(storeId, username);
	}

	/**
	 * 
	 * @param storeId1 the logged in store that is referring another store
	 * @param storeId2 the store that is being referred
	 * @param username Auth Token from pos-auth-api
	 * @return a string value stating that the referral was successful
	 * @throws StoreNotFoundException if no Store with ID storeId is found in the database
	 */
	@PostMapping("/store/{store_id_1}/refers/store/{store_id_2}")
	public String referAStore(@PathVariable(name = "store_id_1") Long storeId1,
							  @PathVariable(name = "store_id_2") Long storeId2,
							  @RequestHeader(name = "user-token") String username
							 )
			throws StoreNotFoundException {
		return storeService.referAStore(storeId1, storeId2, username);

	}

	/**
	 * 
	 * @param storeId the ID of the Store
	 * @param username the username token from pos-auth-api
	 * @return a list of all customer names in all stores owned by a certain user
	 */
	@GetMapping("/store/{store_id}/customers")
	public List<String> getCustomers(@PathVariable(name = "store_id") Long storeId,
									 @RequestHeader(name = "user-token") String username
	                                )
	{
		return storeService.getCustomerList(storeId, username);
	}

	/**
	 * 
	 * @param storeId ID of the Store to get
	 * @param username the username token from pos-auth-api
	 * @return the store based off the provided id
	 * @throws StoreNotFoundException if the store is not found in the database
	 */
	@GetMapping("/store/{store_id}")
	public Store getStore(@PathVariable(name = "store_id") Long storeId,
						  @RequestHeader(name = "user-token") String username
	                     )
			throws StoreNotFoundException {
		return storeService.getById(storeId, username);
	}

	/**
	 * 
	 * @return the list of all available stores in the repository
	 */
	@GetMapping("/stores")
	public List<Store> getStores(@RequestHeader(name = "user-token") String username) {
		return storeService.getAll();
	}

	/**
	 *
	 * @param storeId the ID of the Store to delete
	 * @param username the auth token from pos-auth-api
	 * @return a String message that store with ID storeId has been deleted
	 */
	@DeleteMapping("/store/delete/{store_id}")
	public String deleteStore(@PathVariable(name = "store_id") Long storeId,
								@RequestHeader(name = "user-token") String username) {
		return storeService.delete(storeId, username);
	}

	/**
	 *
	 * @param username user token from pos-auth-api
	 * @return a String message that all Stores have been deleted
	 */
	@DeleteMapping("store/delete/all")
	public String deleteStores(@RequestHeader(name = "user-token") String username) {
		storeService.deleteStores(username);
		return "All stores have been deleted";
	}

	/**
	 * @param storeId ID of Store to upload to
	 * @param file a file to upload
	 * @return the path of the file
	 * @throws StoreNotFoundException if no Store with ID storeId is found in the database
	 */
	@PostMapping("/store-profile/{store_id}")
	public String uploadStoreLogo(@PathVariable(name = "store_id") Long storeId,
								  @RequestPart(value = "file") MultipartFile file,
								  @RequestHeader(name = "user-token") String username
	                             )
			throws StoreNotFoundException, IOException {
		return storeService.uploadStoreLogo(storeId, file, username);
	}

	@DeleteMapping("/store-profile/delete/{store_id}")
	public String deleteStoreImg(@PathVariable(name = "store_id") Long storeId,
								 @RequestPart(value = "url") String fileUrl,
								 @RequestHeader(name = "user-token") String username
	                            )
			throws StoreNotFoundException {
		return storeService.deleteStoreImg(storeId, fileUrl, username);
	}

	/**
	 *
	 * @param file an image file that represents a product in inventory
	 * @param storeId the ID of the Store
	 * @param inventoryId the ID of the product in inventory
	 * @param username the username token from pos-auth-api
	 * @return the URL of the image in S3
	 * @throws StoreNotFoundException if no Store with ID storeId is found in the database
	 */
	@PostMapping("/store/{store_id}/inventory-item/{inventory_id}")
	public String uploadInventoryPhoto(@RequestPart(value = "file") MultipartFile file,
									   @PathVariable(name = "store_id") Long storeId,
									   @PathVariable(name = "inventory_id") Long inventoryId,
									   @RequestHeader(name = "user-token") String username
									  )
			throws StoreNotFoundException {
		return storeService.uploadInventoryPhoto(storeId, inventoryId, file, username);
	}

	@GetMapping("/store/{store_id}/inventory-item/{inventory_id}")
	public String getInventoryPhotoUrl(@PathVariable(name = "store_id") Long storeId,
									   @PathVariable(name = "inventory_id") Long inventoryId,
									   @RequestHeader(name = "user-token") String username
	                                  )

			throws StoreNotFoundException {
		return storeService.getInventoryPhotoUrl(storeId, inventoryId, username);
	}

	@DeleteMapping("/store/{store_id}/inventory_item/delete/{inventory_id}")
	public String deleteProductImg(@PathVariable(name = "store_id") Long storeId,
								   @PathVariable(name = "inventory_id") Long inventoryId,
								   @RequestPart(value = "url") String fileUrl,
								   @RequestHeader(name = "user-token") String username
	                              ) throws StoreNotFoundException {
		return storeService.deleteProductImg(storeId, inventoryId, fileUrl, username);
	}
}
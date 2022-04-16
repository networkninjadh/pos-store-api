package com.howtech.posstoreapi;

import com.howtech.posstoreapi.models.store.Store;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * @author Damond Howard
 */
@SpringBootTest(classes = PosStoreApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PosStoreApiApplicationTests {

	private final Logger LOGGER = LoggerFactory.getLogger(PosStoreApiApplicationTests.class);

	@LocalServerPort private final int port = 8085;

	private final TestRestTemplate restTemplate;
	private final HttpHeaders headers = new HttpHeaders();

	private final String URL;

	 public PosStoreApiApplicationTests() {

		 restTemplate = new TestRestTemplate();
		 URL = "http://localhost:" + port + "/store-api";
		 headers.add("Content-Type", "application/json");
		 headers.add("user-token", "networkninjadh");
		 headers.add("Authorization", "Basic YWRtaW46cGFzcw==");
	 }

	/**
	 *
	 * @throws JSONException if JSON is malformed
	 */
	@Test
	@DisplayName("Create a new Store and verify the information")
	public void addStore() throws JSONException {

		LOGGER.info("PosStoreApiApplicationTests: Creating a new Store");

		String ADD_STORE = "/store/new";

		JSONObject storeDtoJsonObject = new JSONObject();
		storeDtoJsonObject.put("accountManager", "Sarah");
		storeDtoJsonObject.put("account_address_city", "Baton Rouge");
		storeDtoJsonObject.put("account_address_country", "United States");
		storeDtoJsonObject.put("account_address_province", "LA");
		storeDtoJsonObject.put("account_address_street", "1111 Nowhere Dr");
		storeDtoJsonObject.put("account_type", "CHECKING");
		storeDtoJsonObject.put("business_ein", "999999999");
		storeDtoJsonObject.put("business_storeName", "Test Store One");
		storeDtoJsonObject.put("cellphone", "123456789");
		storeDtoJsonObject.put("membershipCode", "BZ1283");
		storeDtoJsonObject.put("openForDelivery", "false");
		storeDtoJsonObject.put("ownerFirstname", "Damond");
		storeDtoJsonObject.put("ownerLastname", "Howard");
		storeDtoJsonObject.put("owner_address_city", "Baton Rouge");
		storeDtoJsonObject.put("owner_address_country", "United States");

		storeDtoJsonObject.put("owner_address_postcode", "70808");
		storeDtoJsonObject.put("owner_address_province", "LA");
		storeDtoJsonObject.put("owner_address_street", "1111 Nowhere Dr");
		storeDtoJsonObject.put("queueFull", "false");
		storeDtoJsonObject.put("storeName", "Test Store One");
		storeDtoJsonObject.put("store_address_city", "Baton Rouge");
		storeDtoJsonObject.put("store_address_country", "United States");
		storeDtoJsonObject.put("store_address_postcode", "70808");
		storeDtoJsonObject.put("store_address_province", "LA");
		storeDtoJsonObject.put("store_address_street", "1111 Nowhere Dr");
		storeDtoJsonObject.put("whenToCharge", "MONTHLY");
		storeDtoJsonObject.put("workphone", "1234567892");

		HttpEntity<String> request =
				new HttpEntity<>(storeDtoJsonObject.toString(), headers);

		ResponseEntity<Store> response = restTemplate
				.postForEntity(URL + ADD_STORE, request, Store.class);

		LOGGER.info("PosStoreApiApplicationTests: Created a new Store");
		Store myStore;
		if (response.getStatusCode() == HttpStatus.OK)
			myStore = response.getBody();
		else
			System.out.println("Something went wrong");
	}

	@Test
	@DisplayName("Retrieve the newly created Store")
	public void getStore() {

		String STORE = "/store/";
		ResponseEntity<Store> response = restTemplate
				.getForEntity(URL + STORE + "1", Store.class);
	}

	@Test
	public void addEmployee() {

	}

	@Test
	public void deleteEmployee() {

	}

	@Test
	public void addInventoryItem() {

	}

	@Test
	public void deleteInventoryItem() {

	}

	@Test
	public void openStore() {

	}

	@Test
	public void closeStore() {

	}

	@Test
	public void deleteStore() {

	}

}

package com.howtech.posstoreapi.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.howtech.posstoreapi.DTOs.CustomerDto;
import com.howtech.posstoreapi.DTOs.HoursDto;
import com.howtech.posstoreapi.DTOs.StoreDto;
import com.howtech.posstoreapi.DTOs.UserInfo;
import com.howtech.posstoreapi.exceptions.CustomerNotFoundException;
import com.howtech.posstoreapi.exceptions.QueueFullException;
import com.howtech.posstoreapi.exceptions.StoreNotFoundException;
import com.howtech.posstoreapi.models.store.AccountAddressInfo;
import com.howtech.posstoreapi.models.store.AccountInfo;
import com.howtech.posstoreapi.models.store.AddressInfo;
import com.howtech.posstoreapi.models.store.BusinessInfo;
import com.howtech.posstoreapi.models.store.Employee;
import com.howtech.posstoreapi.models.store.HoursOfOperation;
import com.howtech.posstoreapi.models.store.Product;
import com.howtech.posstoreapi.models.store.Shipment;
import com.howtech.posstoreapi.models.store.Store;
import com.howtech.posstoreapi.models.store.StoreOrder;
import com.howtech.posstoreapi.models.store.enums.MembershipType;
import com.howtech.posstoreapi.repositories.StoreRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 
 * @author Damond Howard
 * @apiNote this class implements the StoreService for inventory
 */

@Service
public class StoreService {

	Logger LOGGER = LoggerFactory.getLogger(StoreService.class);

	private final StoreRepository storeRepository;
	private final CustomerService customerService;
	private final ValidateMembershipService validateMembershipService;
	private final PaymentService paymentService;
	private final StorageService storageService;

	public StoreService(StoreRepository storeRepository,
			CustomerService customerService,
			ValidateMembershipService validateMembershipService,
			PaymentService paymentService,
			StorageService storageService) {
		this.storeRepository = storeRepository;
		this.customerService = customerService;
		this.validateMembershipService = validateMembershipService;
		this.paymentService = paymentService;
		this.storageService = storageService;
	}

	public Store createFromDto(StoreDto store, String username) {

		LOGGER.info("Creating new Store");
		Store myStore = new Store();
		myStore.getOwners().add(username);
		myStore.setStoreName(store.getStoreName());
		myStore.setAccountManager(store.getAccountManager());
		myStore.setCellPhoneNumber(store.getCellphone());
		myStore.setPhoneNumber(store.getWorkphone());
		myStore.setQueueFull(false);
		myStore.setOpenForDelivery(false);
		myStore.setMembershipType(store.getMembershipType());
		myStore.setAccountStartDate(LocalDate.now());
		LOGGER.info("Adding address info");
		AddressInfo address = new AddressInfo();
		address.setCity(store.getStore_address_city());
		address.setCountry(store.getStore_address_country());
		address.setPostCode(store.getStore_address_postcode());
		address.setProvince(store.getStore_address_province());
		address.setStreet(store.getStore_address_street());
		myStore.setAddress(address);
		// set the address of the owner
		LOGGER.info("Setting Busuiness and Account info");
		AccountAddressInfo accountAddress = new AccountAddressInfo();
		BusinessInfo businessInfo = new BusinessInfo();
		businessInfo.setEIN(store.getBusiness_ein());
		businessInfo.setStoreName(store.getStoreName());
		myStore.setBusinessInfo(businessInfo);
		accountAddress.setCity(store.getAccount_address_city());
		accountAddress.setCountry(store.getAccount_address_country());
		accountAddress.setPostCode(store.getAccount_address_postcode());
		accountAddress.setProvince(store.getAccount_address_province());
		accountAddress.setStreet(store.getAccount_address_street());
		AccountInfo accountInfo = new AccountInfo();
		accountInfo.setAccountAddressInfo(accountAddress);
		myStore.setAccountInfo(accountInfo);
		//myStore.setOwnerName(userInfo.getUsername());
		myStore.setOwnerName(username);
		LOGGER.info("Validating membership code");
		if (store.getMembershipType().equals(MembershipType.BRONZE)) {
			if (validateMembershipService.verifyMembershipBronze(store.getMembershipCode())) {
				myStore.setMembershipType(MembershipType.BRONZE);
				myStore.setWhenToCharge(store.getWhenToCharge());
				myStore.setNextBillingDate();
				return storeRepository.save(myStore);
			} else {
				throw new AccessDeniedException("Membership code is wrong");
			}
		} else if (store.getMembershipType().equals(MembershipType.GOLD)) {
			if (validateMembershipService.verifyMembershipGold(store.getMembershipCode())) {
				myStore.setMembershipType(MembershipType.GOLD);
				myStore.setWhenToCharge(store.getWhenToCharge());
				myStore.setNextBillingDate();
				return storeRepository.save(myStore);
			} else {
				throw new AccessDeniedException("Membership code is wrong");
			}
		} else if (store.getMembershipType().equals(MembershipType.PLATINUM)) {
			if (validateMembershipService.verifyMembershipPlatinum(store.getMembershipCode())) {
				myStore.setMembershipType(MembershipType.PLATINUM);
				myStore.setWhenToCharge(store.getWhenToCharge());
				myStore.setNextBillingDate();
				return storeRepository.save(myStore);
			} else {
				throw new AccessDeniedException("Membership code is wrong");
			}
		} else {
			throw new AccessDeniedException("Membership does not exist");
		}
	}

	public Store addStoreHours(Long storeId, UserInfo userInfo, HoursDto hours) throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		if (myStore.getOwnerName().equals(userInfo.getUsername())) {
			HoursOfOperation storeHours = new HoursOfOperation(
					LocalTime.of(hours.getMonOpenHour(), 0, 0, 0), LocalTime.of(hours.getTueOpenHour(), 0, 0, 0),
					LocalTime.of(hours.getWedOpenHour(), 0, 0, 0),
					LocalTime.of(hours.getThuOpenHour(), 0, 0, 0), LocalTime.of(hours.getFriOpenHour(), 0, 0, 0),
					LocalTime.of(hours.getSatOpenHour(), 0, 0, 0),
					LocalTime.of(hours.getSunOpenHour(), 0, 0, 0), LocalTime.of(hours.getMonCloseHour(), 0, 0, 0),
					LocalTime.of(hours.getTueCloseHour(), 0, 0, 0),
					LocalTime.of(hours.getWedCloseHour(), 0, 0, 0), LocalTime.of(hours.getThuCloseHour(), 0, 0, 0),
					LocalTime.of(hours.getFriCloseHour(), 0, 0, 0),
					LocalTime.of(hours.getSatCloseHour(), 0, 0, 0), LocalTime.of(hours.getSunCloseHour(), 0, 0, 0),
					hours.isOpenForNewYearsEve(), LocalTime.of(hours.getNewYearsEveOpen(), 0, 0, 0),
					LocalTime.of(hours.getNewYearsEveClose(), 0, 0, 0),
					hours.isOpenForNewYears(), LocalTime.of(hours.getNewYearsOpen(), 0, 0, 0),
					LocalTime.of(hours.getNewYearsClose(), 0, 0, 0),
					hours.isOpenForIndependenceDay(), LocalTime.of(hours.getIndependenceOpen(), 0, 0, 0),
					LocalTime.of(hours.getIndependenceClose(), 0, 0, 0),
					hours.isOpenForMemorialDay(), LocalTime.of(hours.getMemorialDayOpen(), 0, 0, 0),
					LocalTime.of(hours.getMemorialDayClose(), 0, 0, 0),
					hours.isOpenForEaster(), LocalTime.of(hours.getEasterOpen(), 0, 0, 0),
					LocalTime.of(hours.getEasterClose(), 0, 0, 0),
					hours.isOpenForColumbusDay(), LocalTime.of(hours.getColumbusDayOpen(), 0, 0, 0),
					LocalTime.of(hours.getColumbusDayClose(), 0, 0, 0),
					hours.isOpenForThanksgiving(), LocalTime.of(hours.getThanksgivingOpen(), 0, 0, 0),
					LocalTime.of(hours.getThanksgivingClose(), 0, 0, 0),
					hours.isOpenForChristmasEve(), LocalTime.of(hours.getChristmasEveOpen(), 0, 0, 0),
					LocalTime.of(hours.getChristmasEveClose(), 0, 0, 0),
					hours.isOpenForChristmas(), LocalTime.of(hours.getChristmasOpen(), 0, 0, 0),
					LocalTime.of(hours.getChristmasClose(), 0, 0, 0), myStore);
			myStore.setHoursOfOperation(storeHours);
			storeRepository.save(myStore);
			return myStore;
		} else {
			throw new AccessDeniedException("You don't own this store so you don't have permission to open it");
		}
	}

	public List<Store> getByOwnerName(UserInfo userInfo) {
		List<Store> stores = storeRepository.findAll();
		Predicate<Store> byOwner = store -> store.getOwnerName().equals(userInfo.getUsername());
		return stores.stream().filter(byOwner)
				.collect(Collectors.toList());
	}

	public Store getById(Long storeId) throws StoreNotFoundException {
		return storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
	}

	public List<Store> getAll() {
		return storeRepository.findAll();
	}

	public String delete(Long storeId) {
		storeRepository.deleteById(storeId);
		return "Store with id " + storeId + " has been deleted";
	}

	public String deleteStores() {
		storeRepository.deleteAll();
		return "All stores have been deleted";
	}

	public List<String> getCustomerList(Long storeId, UserInfo userInfo) {
		Predicate<Store> byOwner = store -> store.getOwnerName().equals(userInfo.getUsername());
		List<Store> myStores = storeRepository
				.findAll()
				.parallelStream()
				.filter(byOwner)
				.collect(Collectors.toList());

		List<String> customerNames = new ArrayList<>();
		myStores
				.parallelStream()
				.forEach(store -> {
					store.getStoreOrders()
							.parallelStream()
							.forEach(order -> {
								customerNames.add(order.getCustomerName());
							});
				});
		return customerNames;
	}

	public Set<Employee> getEmployees(UserInfo userInfo) {
		List<Store> stores = storeRepository.findAll();
		Predicate<Store> byOwner = store -> store.getOwnerName().equals(userInfo.getUsername());
		List<Store> myStores = stores.stream().filter(byOwner).collect(Collectors.toList());
		Set<Employee> myEmployees = new HashSet<>();
		myStores.forEach(store -> {
			store.getEmployees().forEach(ele -> {
				System.out.println(ele.getEmployeeId());
				myEmployees.add(ele);
			});
		});
		return myEmployees;
	}

	public Set<Product> getInventory(UserInfo userInfo) {
		List<Store> stores = storeRepository.findAll();
		Predicate<Store> byOwner = store -> store.getOwnerName().equals(userInfo.getUsername());
		List<Store> myStores = stores.stream().filter(byOwner).collect(Collectors.toList());
		Set<Product> myInventory = new HashSet<>();
		myStores.stream().forEach(store -> {
			store.getStoreInventory().stream().forEach(ele -> {
				myInventory.add(ele);
			});
		});
		return myInventory;
	}

	public Set<StoreOrder> getOrders(UserInfo userInfo) {
		List<Store> stores = storeRepository.findAll();
		Predicate<Store> byOwner = store -> store.getOwnerName().equals(userInfo.getUsername());
		List<Store> myStores = stores.stream().filter(byOwner).collect(Collectors.toList());
		Set<StoreOrder> orders = new HashSet<>();
		myStores.forEach(store -> {
			orders.addAll(store.getStoreOrders());
		});
		return orders;
	}

	public Set<CustomerDto> getCustomers(UserInfo userInfo) {
		List<Store> stores = storeRepository.findAll();
		Predicate<Store> byOwner = store -> store.getOwnerName().equals(userInfo.getUsername());
		Set<Store> myStores = stores.stream().filter(byOwner).collect(Collectors.toSet());
		// TODO get all customers from all stores
		return new HashSet<>();// TODO: finish
		// doesn't need to be done yet do this after the customer side is done
		// use customer to store dto
		// use the customer id in the order
		// pull the customer out of customer repo
		// build the dto objects based off customer
	}

	public String referAStore(Long refererId, Long referedId, UserInfo userInfo) throws StoreNotFoundException {
		Store myStore = storeRepository.findById(refererId)
				.orElseThrow(() -> new StoreNotFoundException(refererId));
		if (myStore.getOwnerName().equals(userInfo.getUsername())) {
			// check to see if the other store has finished signing up first
			Store referredStore = storeRepository.findById(referedId)
					.orElseThrow(() -> new StoreNotFoundException(referedId));
			if (referredStore.getMembershipType() != null) {
				myStore.addReferal(); // can be modified later to keep track of the store that was refered
				storeRepository.save(myStore);
			}
		} else {
			throw new AccessDeniedException("You don't own this store so you cannot refer another store");
		}
		return "Congratulations " + userInfo.getUsername() + " you have successfully referred store " + referedId;
	}

	public String openStore(Long storeId, UserInfo userInfo, Employee employee) throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		if (myStore.getOwnerName().equals(userInfo.getUsername())) {
			Set<Employee> storeEmployees = myStore.getEmployees();
			Predicate<Employee> byId = storeEmployee -> storeEmployee.getEmployeeId().equals(employee.getEmployeeId());
			Employee e = storeEmployees
					.stream()
					.filter(byId)
					.collect(Collectors.toSet())
					.iterator().next();
			if (!e.getCode().equals(employee.getCode())) {
				throw new AccessDeniedException("Your employee code is incorrect");
			}
			myStore.setOpenForDelivery(true);
		} else {
			throw new AccessDeniedException("You don't own this store so you don't have permission to open it");
		}
		return "Congratulations " + userInfo.getUsername() + " your store is now open for business!!!";
	}

	public String closeStore(Long storeId, UserInfo userInfo) throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));

		if (validStoreOwner(myStore, userInfo)) {
			myStore.setOpenForDelivery(false);
			storeRepository.save(myStore);
		} else {
			throw new AccessDeniedException("You don't own this store so you don't have permissions to access it");
		}
		return "Congratulations " + userInfo.getUsername() + " your store is now open for business!!!";
	}

	public Set<StoreOrder> getOrdersByStore(Long storeId, UserInfo userInfo) throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		Set<StoreOrder> orders;
		if (validStoreOwner(myStore, userInfo)) {
			orders = myStore.getStoreOrders();
		} else {
			throw new AccessDeniedException("You don't own this store so you don't have permissions to access it");
		}
		return orders;
	}

	private boolean validStoreOwner(Store myStore, UserInfo userInfo) {
		if (myStore.getOwnerName().equals(userInfo.getUsername()))
			return true;
		return false;
	}

	public Shipment fulfillOrder(Long storeId, Long orderId, Employee employee, UserInfo userInfo)
			throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		if (!myStore.getOwnerName().equals(userInfo.getUsername())) {
			throw new AccessDeniedException("You do not have the auathority to access this resource");
		}
		Set<Employee> storeEmployees = myStore.getEmployees();
		Predicate<Employee> byId = storeEmployee -> storeEmployee.getEmployeeId().equals(employee.getEmployeeId());
		Employee e = storeEmployees
				.parallelStream()
				.filter(byId)
				.collect(Collectors.toSet())
				.iterator().next();
		if (!e.getCode().equals(employee.getCode())) {
			throw new AccessDeniedException("Your employee code is incorrect");
		}
		Shipment myShipment = new Shipment();
		myShipment.setEmployeeId(employee.getEmployeeId());
		myShipment.setOrderId(orderId);
		myShipment.setStore(myStore);
		myShipment.setStoreId(storeId);
		myStore.addShipment(myShipment);
		storeRepository.save(myStore);
		return myShipment;
		// TODO take the order and calculate the prices based off the cart items array
		// each item has an id from the stores inventory
		// TODO verify payment for the order
		// charge the customer
	}

	public StoreOrder orderFrom(Long storeId, Long customerId, StoreOrder order, UserInfo userInfo)
			throws CustomerNotFoundException, StoreNotFoundException, QueueFullException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		CustomerDto customer = customerService.getById(customerId);
		if (myStore.isQueueFull()) {
			throw new QueueFullException(storeId);
		}
		order.setCustomerName(customer.getUsername());
		myStore.addOrder(order);
		storeRepository.save(myStore);
		return order;
		// TODO go into storeOrder get the id of the inventory and use that to decrement
		// the amount of that inventory by the ammount requested
	}

	public Store addEmployee(Long storeId, Employee newEmployeeData, UserInfo userInfo)
			throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		Employee employee = new Employee();
		Set<Employee> employees = myStore.getEmployees();
		employee.setCode(newEmployeeData.getCode());
		employee.setEmail(newEmployeeData.getEmail());
		employee.setEmployeeUserId(newEmployeeData.getEmployeeUserId());
		employee.setFirstName(newEmployeeData.getFirstName());
		employee.setMiddleName(newEmployeeData.getMiddleName());
		employee.setLastName(newEmployeeData.getLastName());
		employee.setStore(myStore);
		employee.setEmployeePermissions(newEmployeeData.getEmployeePermissions());
		employees.add(employee);
		myStore.setEmployees(employees);
		return storeRepository.save(myStore);
	}

	public Employee getEmployee(Long storeId, Long employeeId, UserInfo userInfo) throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		Set<Employee> employees = myStore.getEmployees();
		Predicate<Employee> byId = employee -> employee.getEmployeeId().equals(employeeId);
		Set<Employee> employee = employees.stream().filter(byId).collect(Collectors.toSet());
		return employee.iterator().next();
	}

	public Store changeEmployeeData(Long storeId, Long employeeId, Employee newEmployeeData, UserInfo userInfo)
			throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		Set<Employee> employees = myStore.getEmployees();
		employees
				.forEach((employee) -> {
					if (employee.getEmployeeId().equals(employeeId)) {
						employee.setCode(newEmployeeData.getCode());
						employee.setEmail(newEmployeeData.getEmail());
						employee.setEmployeeUserId(newEmployeeData.getEmployeeUserId());
						employee.setFirstName(newEmployeeData.getFirstName());
						employee.setMiddleName(newEmployeeData.getMiddleName());
						employee.setLastName(newEmployeeData.getLastName());
					}
				});
		myStore.setEmployees(employees);
		return storeRepository.save(myStore);
	}

	public Set<Employee> getEmployees(Long storeId, UserInfo userInfo) throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		return myStore.getEmployees();
	}

	public Store deleteEmployee(Long storeId, Long employeeId, UserInfo userInfo) throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		Set<Employee> employees = myStore.getEmployees();
		employees
				.forEach((employee) -> {
					if (employee.getEmployeeId().equals(employeeId)) {
						employees.remove(employee);
					}
				});
		myStore.setEmployees(employees);
		return storeRepository.save(myStore);
	}

	public Store deleteEmployees(Long storeId, UserInfo userInfo) throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));

		myStore.setEmployees(new HashSet<>());
		return storeRepository.save(myStore);
	}

	public Set<Product> addInventoryItem(Long storeId, Product product, UserInfo userInfo)
			throws StoreNotFoundException {
		Product newProduct = new Product();
		newProduct.setImageURL(product.getImageURL());
		newProduct.setProductDescription(product.getProductDescription());
		newProduct.setProductName(product.getProductName());
		newProduct.setProductPrice(product.getProductPrice());
		newProduct.setProductQuantity(product.getProductQuantity());
		newProduct.setProductType(product.getProductType());
		newProduct.setDescriptions(product.getDescriptions());
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		if (!myStore.getOwnerName().equals(userInfo.getUsername())) {
			throw new AccessDeniedException("You do not have the authority to access this resource");
		}
		newProduct.setStore(myStore);
		myStore.addInventory(newProduct);
		storeRepository.save(myStore);
		return myStore.getStoreInventory();
	}

	public Product getInventoryItem(Long storeId, Long inventoryId, UserInfo userInfo)
			throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		if (!myStore.getOwnerName().equals(userInfo.getUsername())) {
			throw new AccessDeniedException("You do not have the authority to access this resource");
		}
		Set<Product> inventoryItems = myStore.getStoreInventory();
		Predicate<Product> byId = inventoryItem -> inventoryItem.getProductId().equals(inventoryId);
		return inventoryItems.stream().filter(byId).collect(Collectors.toSet()).iterator().next();
	}

	public Store changeInventoryItem(Long storeId, Long inventoryId, Product product, UserInfo userInfo)
			throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		if (!myStore.getOwnerName().equals(userInfo.getUsername())) {
			throw new AccessDeniedException("You do not have the authority to access this resource");
		}

		myStore
				.getStoreInventory()
				.forEach((inventoryItem) -> {
					if (inventoryItem.getProductId().equals(inventoryId)) {
						inventoryItem.setDescriptions(product.getDescriptions());
						inventoryItem.setImageURL(product.getImageURL());
						inventoryItem.setProductBrand(product.getProductBrand());
						inventoryItem.setProductCategory(product.getProductCategory());
						inventoryItem.setProductDescription(product.getProductDescription());
						inventoryItem.setProductName(product.getProductName());
						inventoryItem.setProductPrice(product.getProductPrice());
						inventoryItem.setProductQuantity(product.getProductQuantity());
						inventoryItem.setProductSubCategory(product.getProductSubCategory());
						inventoryItem.setProductType(product.getProductType());
						inventoryItem.setProductUnit(product.getProductUnit());
					}
				});
		return storeRepository.save(myStore);
	}

	public Set<Product> getInventory(Long storeId, UserInfo userInfo) throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		if (!myStore.getOwnerName().equals(userInfo.getUsername())) {
			throw new AccessDeniedException("You do not have the authority to access this resource");
		}
		return myStore.getStoreInventory();
	}

	public String deleteInventoryItem(Long storeId, Long inventoryId, UserInfo userInfo)
			throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		if (!myStore.getOwnerName().equals(userInfo.getUsername())) {
			throw new AccessDeniedException("You do not have the authority to access this resource");
		}

		myStore
				.getStoreInventory()
				.forEach((inventoryItem) -> {
					if (inventoryItem.getProductId().equals(inventoryId)) {
						myStore.getStoreInventory().remove(inventoryItem);
					}
				});
		storeRepository.save(myStore);
		return "Item with store id " + storeId + " and inventory id " + inventoryId + " has been deleted";
	}

	public String deleteInventory(Long storeId, UserInfo userInfo) throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
		myStore.setStoreInventory(null);
		storeRepository.save(myStore);
		return "All of store " + storeId + "'s inventory has been deleted";
	}

	/*
	 * public RedirectView connectToPaypal(Long storeId, HttpSession session,
	 * UserInfo)
	 * throws StoreNotFoundException, PayPalRESTException {
	 * Store myStore = storeRepository.findById(storeId)
	 * .orElseThrow(() -> new StoreNotFoundException(storeId));
	 * if (myStore.getOwnerName().equals(userInfo.getUsername())) {
	 * return paypalService.connectPaypal(session, storeId);
	 * } else {
	 * throw new AccessDeniedException(
	 * "You don't own this store so you don't have permission to connect to paypal"
	 * );
	 * }
	 * }
	 */
	/**
	 * public String paypalAuthRedirect(String authCode, HttpSession session,
	 * UserInfo userInfo)
	 * throws PayPalRESTException {
	 * paypalService.authorizeconnection(authCode, session);
	 * return "Success";
	 * }
	 * 
	 * public void sendPaypalToStore(Long storeId, ChargeDto charge, UserInfo
	 * userInfo) throws IOException {
	 * paypalService.createPaypalPayout(charge.getAmount(), charge.getOrderId(),
	 * storeId);
	 * }
	 **/
	/**
	 * public RedirectView connectToQuickbooks(Long storeId, HttpSession session,
	 * UserInfo userInfo)
	 * throws StoreNotFoundException {
	 * Store myStore = storeRepository.findById(storeId)
	 * .orElseThrow(() -> new StoreNotFoundException(storeId));
	 * if (myStore.getOwnerName().equals(userInfo.getUsername())) {
	 * return qboService.connectToQuickbooks(session, storeId);
	 * } else {
	 * throw new AccessDeniedException(
	 * "You don't own this store so you don't have permission to connect to
	 * quickbooks");
	 * }
	 * }
	 * 
	 * 
	 * public String callBackFromOAuth(String authCode, String state, String
	 * realmId, HttpSession session,
	 * UserInfo userInfo) {
	 * return qboService.callBackFromOAuth(authCode, state, realmId, session);
	 * }
	 * 
	 * public String callQboVendorCreate(Long storeId, HttpSession session,
	 * CompanyDto company, UserInfo userInfo)
	 * throws JsonProcessingException, FMSException, ParseException,
	 * java.text.ParseException {
	 * return qboService.createQboVendor(storeId, company);
	 * }
	 * 
	 * public String callBillingConcept(Long storeId, HttpSession session, ChargeDto
	 * chargedto, UserInfo userInfo)
	 * throws FMSException, ParseException, java.text.ParseException {
	 * return qboService.callBillingConcept(storeId, chargedto.getAmount());
	 * }
	 * 
	 * public ResponseEntity<String> chargeStoreBankAccount(Long storeId, ChargeDto
	 * stripeCharge,
	 * UserInfo userInfo) {
	 * return paymentService.createPaymentwithDefaultSource(storeId, stripeCharge);
	 * }
	 * 
	 * public void createStoreBillingAccount(Long storeId, PayerDto acc, UserInfo
	 * userInfo) throws StripeException {
	 * Customer customer = paymentService.createPayer(storeId, acc);
	 * StripeBank stripeBank = paymentService.createBillingBankAccount(storeId);
	 * paymentService.createStripeCustomerBankAccount(stripeBank.getBankId(), acc);
	 * paymentService.validateStripeBankAccount(stripeBank.getBankId());
	 * }
	 * 
	 * public void createStoreDepositAccount(Long storeId, CompanyDto acc,
	 * HttpServletRequest request,
	 * UserInfo userInfo) {
	 * try {
	 * paymentService.createVendorCompany(acc, request.getRemoteAddr(), storeId);
	 * paymentService.createVendorCompanyBankAccount(storeId, acc);
	 * 
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * }
	 * }
	 * 
	 * public void payStoreDepositAccount(Long storeId, ChargeDto c,
	 * HttpServletRequest request, UserInfo userInfo) {
	 * try {
	 * paymentService.stripeDepositToBank(c.getAmount(), storeId,
	 * "py_1HXH3fDNHKH66w8gMT9OzMvi");
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * }
	 * }
	 */
	public String uploadStoreLogo(Long storeId, MultipartFile file, UserInfo userInfo)
			throws StoreNotFoundException {
		String link = ""; // this.amazonClient.uploadFile(file);
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(userInfo.getUsername()));
		if (!authenticateStoreOwner(userInfo.getUsername(), myStore.getOwners())) {
			throw new AccessDeniedException("This is not your store");
		}
		// myStore.setStoreLogo(link);
		storeRepository.save(myStore);
		return link;
	}

	private boolean authenticateStoreOwner(String username, List<String> owners) {
		if (owners.contains(username)) {
			return true;
		} else {
			return false;
		}
	}

	public URL getStoreLogoUrl(Long storeId, UserInfo userInfo)
			throws StoreNotFoundException, MalformedURLException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(userInfo.getUsername()));
		if (!authenticateStoreOwner(userInfo.getUsername(), myStore.getOwners())) {
			throw new AccessDeniedException("This is not your store");
		}
		String link = myStore.getStoreLogo();
		return new URL(link); // amazonClient.getFileUrl(link);
	}

	public String deleteStoreImg(Long storeId, String fileUrl, UserInfo userInfo) throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(userInfo.getUsername()));
		if (!authenticateStoreOwner(userInfo.getUsername(), myStore.getOwners())) {
			throw new AccessDeniedException("This is not your store");
		}
		myStore.setStoreLogo(null);
		// return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
		return "";
	}

	public String uploadInventoryPhoto(Long storeId, Long inventoryId, MultipartFile file, UserInfo userInfo)
			throws StoreNotFoundException {
		String link = ""; // this.amazonClient.uploadFile(file);
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(userInfo.getUsername()));
		if (!authenticateStoreOwner(userInfo.getUsername(), myStore.getOwners())) {
			throw new AccessDeniedException("This is not your store");
		}
		Set<Product> myInventory = myStore.getStoreInventory();
		myInventory
				.stream()
				.forEach((inventoryItem) -> {
					if (inventoryId == inventoryItem.getProductId()) {
						inventoryItem.setImageURL(link);
					}
				});
		myStore.setStoreInventory(myInventory);
		storeRepository.save(myStore);
		return link;
	}

	public String getInventoryPhotoUrl(Long storeId, Long inventoryId, UserInfo userInfo)
			throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(userInfo.getUsername()));
		if (!authenticateStoreOwner(userInfo.getUsername(), myStore.getOwners())) {
			throw new AccessDeniedException("This is not your store");
		}
		Set<Product> myInventory = myStore.getStoreInventory();
		Predicate<Product> byId = product -> product.getProductId().equals(inventoryId);
		Product myProduct = myInventory
				.stream()
				.filter(byId)
				.collect(Collectors.toSet())
				.iterator()
				.next();
		return myProduct.getImageURL();

	}

	public String deleteProductImg(Long storeId, Long inventoryId, String fileUrl, UserInfo userInfo)
			throws StoreNotFoundException {
		Store myStore = storeRepository.findById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(userInfo.getUsername()));
		if (!authenticateStoreOwner(userInfo.getUsername(), myStore.getOwners())) {
			throw new AccessDeniedException("This is not your store");
		}
		Set<Product> myInventory = myStore.getStoreInventory();
		myInventory
				.stream()
				.forEach((inventoryItem) -> {
					if (inventoryId == inventoryItem.getProductId()) {
						inventoryItem.setImageURL(null);
					}
				});
		myStore.setStoreInventory(myInventory);
		storeRepository.save(myStore);
		return ""; // this.amazonClient.deleteFileFromS3Bucket(fileUrl);
	}
}
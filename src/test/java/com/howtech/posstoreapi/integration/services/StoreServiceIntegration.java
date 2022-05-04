package com.howtech.posstoreapi.integration.services;

import com.howtech.posstoreapi.DTOs.HoursDto;
import com.howtech.posstoreapi.DTOs.StoreDto;
import com.howtech.posstoreapi.PosStoreApiApplication;
import com.howtech.posstoreapi.models.store.Store;
import com.howtech.posstoreapi.models.store.enums.AccountType;
import com.howtech.posstoreapi.models.store.enums.ChargeFrequency;
import com.howtech.posstoreapi.models.store.enums.MembershipType;
import com.howtech.posstoreapi.repositories.StoreRepository;
import com.howtech.posstoreapi.services.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalTime;

@SpringBootTest(classes = PosStoreApiApplication.class)
@AutoConfigureDataJpa
public class StoreServiceIntegration {

    private final Logger LOGGER = LoggerFactory.getLogger(StoreServiceIntegration.class);

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private ValidateMembershipService validateMembershipService;

    @Test
    @DisplayName("Create a new Store from StoreDto: should create a valid Store")
    public void createFromDto() {

        MembershipType myMembershipType = MembershipType.BRONZE;
        ChargeFrequency whenToCharge = ChargeFrequency.MONTHLY;
        String membershipCode = "BZ1283";
        AccountType myAccountType = AccountType.CHECKING;

        LOGGER.info("Creating a new Store from DTO object");

        StoreDto storeDto = new StoreDto();

        storeDto.setAccountManager("Sarah");
        storeDto.setAccount_address_city("Baton Rouge");
        storeDto.setAccount_address_country("United States");
        storeDto.setAccount_address_province("LA");
        storeDto.setAccount_address_street("1111 Nowhere Dr");
        storeDto.setAccount_type(myAccountType);
        storeDto.setBusiness_ein("999999999");
        storeDto.setBusiness_storeName("Test Store One");
        storeDto.setCellphone("123456789");
        storeDto.setMembershipCode(membershipCode);
        storeDto.setMembershipType(myMembershipType);
        storeDto.setOpenForDelivery(false);
        storeDto.setOwnerFirstname("Damond");
        storeDto.setOwnerLastname("Howard");
        storeDto.setOwner_address_city("Baton Rouge");
        storeDto.setOwner_address_country("United States");
        storeDto.setOwner_address_postcode("70808");
        storeDto.setOwner_address_province("LA");
        storeDto.setOwner_address_street("1111 Nowhere Dr");
        storeDto.setQueueFull(false);
        storeDto.setStoreName("Test Store One");
        storeDto.setStore_address_city("Baton Rouge");
        storeDto.setStore_address_country("United States");
        storeDto.setStore_address_postcode("70808");
        storeDto.setStore_address_province("LA");
        storeDto.setStore_address_street("1111 Nowhere Dr");
        storeDto.setWhenToCharge(whenToCharge);
        storeDto.setWorkphone("1234567892");

        LOGGER.info("Creating StoreService");

        StoreService storeService = new StoreService(storeRepository, customerService, validateMembershipService, null, null);
        String username = "networkninjadh";

        Store validStore = storeService.createFromDto(storeDto, username);

        assertThat("New Store validStore is a valid Store object", validStore, instanceOf(Store.class));
        assertThat("New Store is NOT null ", validStore, is(notNullValue()));
        assertThat("MembershipType is NOT null", validStore.getMembershipType(), equalTo(myMembershipType));
    }

    @Test
    @DisplayName("Create a new Store from StoreDto: should get AccessDeniedException for incorrect membership code")
    public void createFromDtoWithInvalidMembershipCodeBRONZE() {


        MembershipType myMembershipType = MembershipType.BRONZE;
        ChargeFrequency whenToCharge = ChargeFrequency.MONTHLY;
        String membershipCode = "LI1283";
        AccountType myAccountType = AccountType.CHECKING;

        StoreDto storeDto = new StoreDto();

        storeDto.setAccountManager("Sarah");
        storeDto.setAccount_address_city("Baton Rouge");
        storeDto.setAccount_address_country("United States");
        storeDto.setAccount_address_province("LA");
        storeDto.setAccount_address_street("1111 Nowhere Dr");
        storeDto.setAccount_type(myAccountType);
        storeDto.setBusiness_ein("999999999");
        storeDto.setBusiness_storeName("Test Store One");
        storeDto.setCellphone("123456789");
        storeDto.setMembershipCode(membershipCode);
        storeDto.setMembershipType(myMembershipType);
        storeDto.setOpenForDelivery(false);
        storeDto.setOwnerFirstname("Damond");
        storeDto.setOwnerLastname("Howard");
        storeDto.setOwner_address_city("Baton Rouge");
        storeDto.setOwner_address_country("United States");
        storeDto.setOwner_address_postcode("70808");
        storeDto.setOwner_address_province("LA");
        storeDto.setOwner_address_street("1111 Nowhere Dr");
        storeDto.setQueueFull(false);
        storeDto.setStoreName("Test Store One");
        storeDto.setStore_address_city("Baton Rouge");
        storeDto.setStore_address_country("United States");
        storeDto.setStore_address_postcode("70808");
        storeDto.setStore_address_province("LA");
        storeDto.setStore_address_street("1111 Nowhere Dr");
        storeDto.setWhenToCharge(whenToCharge);
        storeDto.setWorkphone("1234567892");

        StoreService storeService = new StoreService(storeRepository, customerService, validateMembershipService, paymentService, storageService);
        String username = "networkninjadh";

        Throwable exception = assertThrows(AccessDeniedException.class, ( ) -> storeService.createFromDto(storeDto, username));
    }

    @Test
    @DisplayName("Create a new Store from StoreDto: should get AccessDeniedException for incorrect membership code GOLD")
    public void createFromDtoWithInvalidMembershipCodeGOLD() {


        MembershipType myMembershipType = MembershipType.GOLD;
        ChargeFrequency whenToCharge = ChargeFrequency.MONTHLY;
        String membershipCode = "LI1283";
        AccountType myAccountType = AccountType.CHECKING;

        StoreDto storeDto = new StoreDto();

        storeDto.setAccountManager("Sarah");
        storeDto.setAccount_address_city("Baton Rouge");
        storeDto.setAccount_address_country("United States");
        storeDto.setAccount_address_province("LA");
        storeDto.setAccount_address_street("1111 Nowhere Dr");
        storeDto.setAccount_type(myAccountType);
        storeDto.setBusiness_ein("999999999");
        storeDto.setBusiness_storeName("Test Store One");
        storeDto.setCellphone("123456789");
        storeDto.setMembershipCode(membershipCode);
        storeDto.setMembershipType(myMembershipType);
        storeDto.setOpenForDelivery(false);
        storeDto.setOwnerFirstname("Damond");
        storeDto.setOwnerLastname("Howard");
        storeDto.setOwner_address_city("Baton Rouge");
        storeDto.setOwner_address_country("United States");
        storeDto.setOwner_address_postcode("70808");
        storeDto.setOwner_address_province("LA");
        storeDto.setOwner_address_street("1111 Nowhere Dr");
        storeDto.setQueueFull(false);
        storeDto.setStoreName("Test Store One");
        storeDto.setStore_address_city("Baton Rouge");
        storeDto.setStore_address_country("United States");
        storeDto.setStore_address_postcode("70808");
        storeDto.setStore_address_province("LA");
        storeDto.setStore_address_street("1111 Nowhere Dr");
        storeDto.setWhenToCharge(whenToCharge);
        storeDto.setWorkphone("1234567892");

        StoreService storeService = new StoreService(storeRepository, customerService, validateMembershipService, paymentService, storageService);
        String username = "networkninjadh";

        Throwable exception = assertThrows(AccessDeniedException.class, ( ) -> storeService.createFromDto(storeDto, username));
    }
    @Test
    @DisplayName("Add hours of operation to a Store: should return a store Object with the hours added")
    public void addStoreHours() {

        StoreService storeService = new StoreService(storeRepository, customerService, validateMembershipService, paymentService, storageService);

        HoursDto storeHours = new HoursDto(7,19,7,19,7,19,
                 7,19, 7,19,7, 19,
                 7, 19, true, 7, 19,
                 false, 0, 0, false, 0,
                 0, false, 0, 0, false,
                 0, 0, false, 0, 0, true,
                 7, 19, false, 0, 0,
                 false, 0, 0);

        Store storeWithHours = storeService.addStoreHours(1L, "networkninjadh", storeHours);

        assertThat("Store opens at 7:00 on Monday: monOpenHour = 7", storeWithHours.getStoreHours().getMondayOpen(), equalTo(LocalTime.of(7, 0, 0,0)));
        assertThat("Store closes at 19:00 on Monday: monCloseHour = 19", storeWithHours.getStoreHours().getMondayClose(), equalTo(LocalTime.of(19, 0, 0, 0)));
        assertThat("Store is open on New Years Eve: openForNewYearsEve = true", storeWithHours.getStoreHours().isOpenForNewYearsEve(), equalTo(true));
        assertThat("Store is closed on Christmas Day: openForChristmas = false", storeWithHours.getStoreHours().isOpenForChristmas(), equalTo(false));
    }
}

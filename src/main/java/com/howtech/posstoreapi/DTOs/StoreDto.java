package com.howtech.posstoreapi.DTOs;

import com.howtech.posstoreapi.models.store.enums.AccountType;
import com.howtech.posstoreapi.models.store.enums.ChargeFrequency;
import com.howtech.posstoreapi.models.store.enums.MembershipType;

import lombok.Data;

/**
 * 
 * @author Damond Howard
 * @apiNote Data transfer object for creating a new store
 */

@Data
public class StoreDto {
	private String accountManager;
	private String workphone;
	private String cellphone;
	// owner name add to array
	private String ownerFirstname;
	private String ownerLastname;
	// json ignore on pass when returning jwt request

	private String storeName;
	private boolean queueFull;
	private boolean openForDelivery;
	private MembershipType membershipType;
	private String membershipCode;
	private ChargeFrequency whenToCharge;

	// address information
	private String owner_address_street;
	private String owner_address_city;
	private String owner_address_province;
	private String owner_address_country;
	private String owner_address_postcode;

	private String store_address_street;
	private String store_address_city;
	private String store_address_province;
	private String store_address_country;
	private String store_address_postcode;

	// private BusinessInfo businessInfo;

	private String business_ein;
	private String business_storeName;

	// account Address info

	private String account_address_street;
	private String account_address_city;
	private String account_address_province;
	private String account_address_country;
	private String account_address_postcode;

	private AccountType account_type;

}
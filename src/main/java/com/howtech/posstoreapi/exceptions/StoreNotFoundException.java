package com.howtech.posstoreapi.exceptions;

/**
 * 
 * @author Damond Howard
 * @apiNote exception to be thrown when a store does not exist in the database
 */
public class StoreNotFoundException extends Exception {

	/**
	 * ,
	 */
	private static final long serialVersionUID = -7003823387013017735L;
	private String ERROR_MESSAGE;

	/**
	 * 
	 * @param id the id of the store that could not be found in the database
	 */
	public StoreNotFoundException(Long id) {
		ERROR_MESSAGE = "There is no store found with id: " + id;
	}

	public StoreNotFoundException(String username) {
		ERROR_MESSAGE = "There is no store found with username " + username;
	}

	@Override
	/**
	 * returns the error message
	 */
	public String toString() {
		return ERROR_MESSAGE;
	}
}
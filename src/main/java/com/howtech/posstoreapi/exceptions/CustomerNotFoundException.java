package com.howtech.posstoreapi.exceptions;

/**
 * 
 * @author Damond Howard
 * @apiNote Exception to be thrown when a customer is not found in the database
 */
public class CustomerNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6711406069444923545L;
	private String ERROR_MESSAGE;

	/**
	 * 
	 * @param id identifier of the customer that was not found in the database
	 */
	public CustomerNotFoundException(Long id) {
		ERROR_MESSAGE = "There is no customer found with id: " + id;
	}

	public CustomerNotFoundException(String username) {
		ERROR_MESSAGE = "There is no customer found with username " + username;
	}

	/**
	 * returns the error message for the exception
	 */
	@Override
	public String toString() {
		return ERROR_MESSAGE;
	}
}
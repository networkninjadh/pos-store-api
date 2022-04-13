package com.howtech.posstoreapi.exceptions;

/**
 * 
 * @author Damond Howard
 * @apiNote This is an exception to be thrown if an order tries to go to a store
 *          that already has too many orders a store can have a maximum of 15
 *          orders in queue
 *
 */
public class QueueFullException extends Exception {

	private String ERROR_MESSAGE;

	public QueueFullException(Long storeId) {
		this.ERROR_MESSAGE = "Sorry but the store with id = " + storeId + " is at maximum for orders";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return ERROR_MESSAGE;
	}
}
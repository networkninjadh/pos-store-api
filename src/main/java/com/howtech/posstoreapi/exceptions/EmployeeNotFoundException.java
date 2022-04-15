package com.howtech.posstoreapi.exceptions;

public class EmployeeNotFoundException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -2315307823816080980L;

    private String ERROR_MESSAGE;

    public EmployeeNotFoundException(Long employeeId, Long storeId) {
        ERROR_MESSAGE = "Sorry could not find employee with id " + employeeId + "from store with id " + storeId;
    }

    @Override
    public String toString() {
        return ERROR_MESSAGE;
    }
}

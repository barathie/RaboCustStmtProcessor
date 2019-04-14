package com.rabobank.customer.transaction.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exception Class for handling Customer Transaction application
 */
@SuppressWarnings("serial")

public class CustomerTransactionException extends RuntimeException {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerTransactionException.class);

	private  String errorMessage="";
	
	public CustomerTransactionException() {
		super();
	}

	public CustomerTransactionException(String errorMessage, Throwable originalException) {
		super(errorMessage, originalException);
		LOGGER.error("Rabo Bank Application Error :"+errorMessage);
	}
	
	public CustomerTransactionException(String errorMessage) {
		super(errorMessage);
		LOGGER.error("Rabo Bank Application Error :"+errorMessage);
	}	
	
	
	public CustomerTransactionException(Throwable originalException) {
		super(originalException);
	}	

	public String getMessage() {
		return this.errorMessage;
	}

	public void setMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}

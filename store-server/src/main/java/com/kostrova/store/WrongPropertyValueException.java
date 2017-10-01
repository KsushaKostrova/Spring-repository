package com.kostrova.store;

public class WrongPropertyValueException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public WrongPropertyValueException(String message) {		
		super(message);
	}

}

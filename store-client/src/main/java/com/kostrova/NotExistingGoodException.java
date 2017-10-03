package com.kostrova;

public class NotExistingGoodException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public NotExistingGoodException(String message ) {
		super(message);
	}

}

package com.udemy.myfinances.exception;

public class BusinessLogicException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BusinessLogicException(String message) {
		super(message);
	}

}

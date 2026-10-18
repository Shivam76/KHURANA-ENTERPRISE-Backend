package com.khurana.enterprise.exception;

public class ProductNotFoundException extends RuntimeException {
	public ProductNotFoundException(String message) {
		super(message);
	}
}

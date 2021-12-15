package com.devsuperior.bds02.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {

	static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(String msg) {
		super(msg);
	}
}

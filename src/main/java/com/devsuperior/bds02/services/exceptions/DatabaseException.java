package com.devsuperior.bds02.services.exceptions;

public class DatabaseException extends RuntimeException {

	static final long serialVersionUID = 1L;
	
	public DatabaseException(String msg) {
		super(msg);
	}
}

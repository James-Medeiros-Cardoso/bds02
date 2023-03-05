package com.devsuperior.bds02.service.exceptions;

public class DataBaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DataBaseException(String msg) {
        super(msg);
    }

}

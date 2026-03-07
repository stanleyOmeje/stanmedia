package com.stan.customer.exceptions;

public class AlreadyExistException extends RuntimeException{
    private String code;

    public AlreadyExistException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

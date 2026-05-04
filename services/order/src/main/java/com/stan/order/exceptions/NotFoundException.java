package com.stan.order.exceptions;

public class NotFoundException extends RuntimeException{
    private String code;

    public NotFoundException( String code,String message) {
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

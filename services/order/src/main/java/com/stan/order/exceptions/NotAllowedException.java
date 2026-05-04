package com.stan.order.exceptions;

public class NotAllowedException extends RuntimeException{

    private String code;

    public NotAllowedException(String message, String code) {
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

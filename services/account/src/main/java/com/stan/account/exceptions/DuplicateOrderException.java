package com.stan.account.exceptions;


import com.stan.account.enums.ResponseStatus;

public class DuplicateOrderException extends RuntimeException{
    private String code;

    public DuplicateOrderException(String message) {
        super(message);
        this.code = ResponseStatus.ALREADY_EXIST.getCode();
    }

    public DuplicateOrderException(String message, String code) {
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

package com.stan.order.exceptions;

import com.stan.order.enums.ResponseStatus;

public class BadRequestException extends RuntimeException{
    String code;

    public BadRequestException(String message) {
        super(message);
        this.code = ResponseStatus.BAD_REQUEST.getCode();
    }

    public BadRequestException(String message, String code) {
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

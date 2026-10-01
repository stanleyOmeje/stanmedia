package com.stan.gateway.exception;

import com.stan.gateway.enums.ResponseStatus;
import lombok.Data;

@Data
public class NotFoundException extends RuntimeException{
    private String code;
    public NotFoundException(String message) {
        super(message);
        this.code = ResponseStatus.NOT_FOUND.getCode();
    }
    public NotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }

}

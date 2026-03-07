package com.stan.product.product.exception;



import com.stan.product.product.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BadRequestException extends RuntimeException {
    private String code;
    public BadRequestException() {

    }
    public BadRequestException(String message) {
        super(message);
        this.code= ResponseStatus.BAD_REQUEST.getCode();
    }
    public BadRequestException(String code, String message) {
        super(message);
        this.code = code;
    }

}

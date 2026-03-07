package com.stan.product.product.exception;



import com.stan.product.product.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UnAuthorizedException extends RuntimeException {
    private String code;
    public UnAuthorizedException(String message) {
        super(message);
        this.code= ResponseStatus.ALREADY_EXIST.getCode();
    }
    public UnAuthorizedException(String code, String message) {
        super(message);
        this.code = code;
    }

}

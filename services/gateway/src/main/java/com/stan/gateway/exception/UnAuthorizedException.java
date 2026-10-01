package com.stan.gateway.exception;



import com.stan.gateway.enums.ResponseStatus;
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

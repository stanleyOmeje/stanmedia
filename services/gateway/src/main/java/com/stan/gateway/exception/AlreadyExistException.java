package com.stan.gateway.exception;



import com.stan.gateway.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AlreadyExistException extends RuntimeException {
    private String code;
    public AlreadyExistException(String message) {
        super(message);
        this.code= ResponseStatus.ALREADY_EXIST.getCode();
    }
    public AlreadyExistException(String code, String message) {
        super(message);
        this.code = code;
    }

}

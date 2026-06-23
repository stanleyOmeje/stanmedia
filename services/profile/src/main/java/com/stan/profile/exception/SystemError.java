package com.stan.profile.exception;


import com.stan.profile.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SystemError extends RuntimeException {
    private String code;
    public SystemError(String message) {
        super(message);
        this.code = ResponseStatus.BAD_REQUEST.getCode();
    }
    public SystemError(String code, String message) {
        super(message);
        this.code = code;
    }

}

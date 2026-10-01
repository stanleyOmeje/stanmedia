package com.stan.gateway.exception;



import com.stan.gateway.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentException extends RuntimeException {
    private String code;
    public PaymentException(String message) {
        super(message);
        this.code= ResponseStatus.ALREADY_EXIST.getCode();
    }
    public PaymentException(String code, String message) {
        super(message);
        this.code = code;
    }

}

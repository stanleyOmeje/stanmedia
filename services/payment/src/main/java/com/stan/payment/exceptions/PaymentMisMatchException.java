package com.stan.payment.exceptions;


public class PaymentMisMatchException extends RuntimeException{
    private String code;

    public PaymentMisMatchException(String message, String code) {
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

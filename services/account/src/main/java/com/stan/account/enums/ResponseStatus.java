package com.stan.account.enums;

public enum ResponseStatus {
    SUCCESS("00", "Successful"),
    FAILED("01", "Failed"),
    PENDING("03", "Pending"),
    NOT_FOUND("100", "Not Found"),
    CREATED("200", "Created"),
    ALREADY_EXIST("300", "Already Exixt"),
    UNAUTHORIZED("400", "User Not Authorized "),
    SYSTEM_ERROR("500", "System Error"),
    BAD_REQUEST("600", "Bad Request"),
    PAYMENT_FAILED("700", "Payment Failed");

    private String code;
    private String message;
    ResponseStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

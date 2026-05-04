package com.stan.order.dto.response;

import lombok.Data;

@Data
public class DefaultResponse<T> {
    private String status;
    private String message;

    private T data;

    public DefaultResponse() {
    }

    public DefaultResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public DefaultResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}

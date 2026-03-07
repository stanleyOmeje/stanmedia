package com.stan.product.product.exception;


import com.stan.product.product.dto.response.DefaultResponse;
import com.stan.product.product.enums.ResponseStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DefaultResponse<Object> validateInput(MethodArgumentNotValidException ex) {
        DefaultResponse<Object> response = new DefaultResponse<>();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String name = error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
            errors.put(name, error.getDefaultMessage());
        });
        response.setStatus(ResponseStatus.BAD_REQUEST.getCode());
        response.setMessage(ResponseStatus.BAD_REQUEST.getMessage());
        response.setData(errors);
        return response ;
    }

    @ExceptionHandler(NotFoundException.class)
    public DefaultResponse<Object> handleNotFound(NotFoundException ex){
        DefaultResponse<Object> response = new DefaultResponse<>();
        response.setStatus(ex.getCode());
        response.setMessage(ex.getMessage());
        return response ;
    }

    @ExceptionHandler(AlreadyExistException.class)
    public DefaultResponse<Object> handleAlreadyExist(AlreadyExistException ex) {
        DefaultResponse<Object> response = new DefaultResponse<>();
        response.setStatus(ex.getCode());
        response.setMessage(ex.getMessage());
        return response ;
    }

    @ExceptionHandler(BadRequestException.class)
    public DefaultResponse<Object> handleBadRequest(BadRequestException ex) {
        DefaultResponse<Object> response = new DefaultResponse<>();
        response.setStatus(ex.getCode());
        response.setMessage(ex.getMessage());
        return response ;
    }

    @ExceptionHandler(SystemError.class)
    public DefaultResponse<Object> handleSystemError(SystemError ex) {
        DefaultResponse<Object> response = new DefaultResponse<>();
        response.setStatus(ex.getCode());
        response.setMessage(ex.getMessage());
        return response ;
    }


    @ExceptionHandler(UnAuthorizedException.class)
    public DefaultResponse<Object> handleUnAuthorizedException(UnAuthorizedException ex) {
        DefaultResponse<Object> response = new DefaultResponse<>();
        response.setStatus(ex.getCode());
        response.setMessage(ex.getMessage());
        return response ;
    }

    @ExceptionHandler(PaymentException.class)
    public DefaultResponse<Object> handlePaymentException(PaymentException ex) {
        DefaultResponse<Object> response = new DefaultResponse<>();
        response.setStatus(ex.getCode());
        response.setMessage(ex.getMessage());
        return response ;
    }
}

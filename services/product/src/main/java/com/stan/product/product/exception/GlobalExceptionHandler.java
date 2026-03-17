//package com.stan.product.product.exception;
//
//
//import com.stan.product.product.dto.response.DefaultResponse;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.context.request.WebRequest;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@ControllerAdvice
//public class GlobalExceptionHandler{
//
//    @ResponseStatus(HttpStatus.OK)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public DefaultResponse<Object> validateInput(MethodArgumentNotValidException ex) {
//        DefaultResponse<Object> response = new DefaultResponse<>();
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            String name = error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
//            errors.put(name, error.getDefaultMessage());
//        });
//        response.setStatus(ex.getStatusCode().toString());
//        response.setMessage(ex.getMessage());
//        response.setData(errors);
//        return response ;
//    }
//
//    @ResponseStatus(HttpStatus.OK)
//    @ExceptionHandler(NotFoundException.class)
//    public DefaultResponse<Object> handleNotFound(NotFoundException ex){
//        DefaultResponse<Object> response = new DefaultResponse<>();
//        response.setStatus(ex.getCode());
//        response.setMessage(ex.getMessage());
//        return response ;
//    }
//
//    @ResponseStatus(HttpStatus.OK)
//    @ExceptionHandler(AlreadyExistException.class)
//    public DefaultResponse<Object> handleAlreadyExist(AlreadyExistException ex) {
//        DefaultResponse<Object> response = new DefaultResponse<>();
//        response.setStatus(ex.getCode());
//        response.setMessage(ex.getMessage());
//        return response ;
//    }
//
////    @ResponseStatus(HttpStatus.OK)
////    @ExceptionHandler
////    public ResponseEntity<Object> handleCategoryAlreadyExistException(AlreadyExistException ex, WebRequest request) {
////        return new ResponseEntity<>(new DefaultResponse(ex.getMessage(), ex.getCode()), new HttpHeaders(), HttpStatus.OK);
////    }
//
//    @ExceptionHandler(BadRequestException.class)
//    public DefaultResponse<Object> handleBadRequest(BadRequestException ex) {
//        DefaultResponse<Object> response = new DefaultResponse<>();
//        response.setStatus(ex.getCode());
//        response.setMessage(ex.getMessage());
//        return response ;
//    }
//
//    @ResponseStatus(HttpStatus.OK)
//    @ExceptionHandler(SystemError.class)
//    public DefaultResponse<Object> handleSystemError(SystemError ex) {
//        DefaultResponse<Object> response = new DefaultResponse<>();
//        response.setStatus(ex.getCode());
//        response.setMessage(ex.getMessage());
//        return response ;
//    }
//
//
//    @ResponseStatus(HttpStatus.OK)
//    @ExceptionHandler(UnAuthorizedException.class)
//    public DefaultResponse<Object> handleUnAuthorizedException(UnAuthorizedException ex) {
//        DefaultResponse<Object> response = new DefaultResponse<>();
//        response.setStatus(ex.getCode());
//        response.setMessage(ex.getMessage());
//        return response ;
//    }
//
//    @ResponseStatus(HttpStatus.OK)
//    @ExceptionHandler(PaymentException.class)
//    public DefaultResponse<Object> handlePaymentException(PaymentException ex) {
//        DefaultResponse<Object> response = new DefaultResponse<>();
//        response.setStatus(ex.getCode());
//        response.setMessage(ex.getMessage());
//        return response ;
//    }
//}

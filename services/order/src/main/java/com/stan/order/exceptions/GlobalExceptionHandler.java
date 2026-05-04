package com.stan.order.exceptions;




import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.stan.order.dto.response.DefaultResponse;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleAreadyExistException(AlreadyExistException ex, WebRequest request){
      return new ResponseEntity<>(new DefaultResponse<>(ex.getCode(),ex.getMessage()),new HttpHeaders(), HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleNotFoundExistException(NotFoundException ex, WebRequest request){
        return new ResponseEntity<>(new DefaultResponse<>(ex.getCode(),ex.getMessage()),new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request){
        return new ResponseEntity<>(new DefaultResponse<>(ex.getCode(),ex.getMessage()),new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleNotAllowedException(NotAllowedException ex, WebRequest request){
        return new ResponseEntity<>(new DefaultResponse<>(ex.getCode(),ex.getMessage()),new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}

package com.emobile.springtodo.handler;

import com.emobile.springtodo.dto.response.ExceptionResponse;
import com.emobile.springtodo.exception.AccountAlreadyExistsException;
import com.emobile.springtodo.exception.AccountNotFoundException;
import com.emobile.springtodo.exception.SQLProcessChangesException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ AccountAlreadyExistsException.class, AccountNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequestException(Exception ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SQLProcessChangesException.class, Exception.class})
    public ResponseEntity<ExceptionResponse> handleInternalServerErrorException(Exception ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

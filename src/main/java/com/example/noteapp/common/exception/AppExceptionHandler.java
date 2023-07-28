package com.example.noteapp.common.exception;

import com.example.noteapp.common.ResponseHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleDataAccessNotFound() {
        return ResponseHandler.generateResponse("Empty Result Data Access", HttpStatus.NOT_FOUND,  null);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFound ex) {
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_FOUND,  null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation() {
        return ResponseHandler.generateResponse("Data Integrity Violation", HttpStatus.CONFLICT,  null);
    }
}

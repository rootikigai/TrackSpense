package com.trackspense.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MasterExceptionHandler{

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex){
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExists(EmailAlreadyExistsException ex){
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPassword(InvalidPasswordException ex){
        return buildResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidEmailFormat(InvalidEmailFormatException ex){
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex){
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex){
        return buildResponse("An unexpected error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus status) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", status.value());
        errorBody.put("error", status.getReasonPhrase());
        errorBody.put("message", message);
        return new ResponseEntity<>(errorBody, status);
    }
}

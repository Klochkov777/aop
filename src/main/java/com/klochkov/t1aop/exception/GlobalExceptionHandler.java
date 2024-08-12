package com.klochkov.t1aop.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Getter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorResponse {
        private int status;
        private String error;
        private String message;
    }

    @Getter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorArrayResponse {
        private int status;
        private String error;
        private String[] message;
    }

    protected static ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus httpStatus, String message) {
        ErrorResponse response = new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), message);
        return ResponseEntity.status(httpStatus.value()).body(response);
    }

    protected static ResponseEntity<ErrorArrayResponse> buildErrorArrayResponse(String[] message) {
        ErrorArrayResponse response = new ErrorArrayResponse(
                HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorArrayResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Set<String> errorsSet = new HashSet<>();
        ex.getBindingResult().getAllErrors()
                .forEach(error -> {
                    String errorMessage = error.getDefaultMessage();
                    errorsSet.add(errorMessage);
                });
        return buildErrorArrayResponse(errorsSet.toArray(new String[0]));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorArrayResponse> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        Set<String> errorsSet = new HashSet<>();
        ex.getConstraintViolations().forEach(error -> {
            String errorMessage = error.getMessage();
            errorsSet.add(errorMessage);
        });
        return buildErrorArrayResponse(errorsSet.toArray(new String[0]));
    }
}

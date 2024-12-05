package com.tkachenya.currency.exception;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {
    protected final Logger logger = getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GenericError> handleRuntimeException(Exception ex, WebRequest request) {
        logger.error(ex.getMessage());

        var error = new GenericError();
        error.code = "Runtime error";
        error.message = ex.getMessage();

        return new ResponseEntity<>(error, INTERNAL_SERVER_ERROR);
    }
}

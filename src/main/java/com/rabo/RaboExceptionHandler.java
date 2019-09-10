package com.rabo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RaboExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomerStatementException.class)
    public ResponseEntity<Object> handleParseException(CustomerStatementException customerStatementException) {
        log.info(customerStatementException.getMessage());
        return new ResponseEntity<>(CustomerStatementException.MESSAGE, CustomerStatementException.HTTP_STATUS);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        return new ResponseEntity<>("An unkown error occured", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

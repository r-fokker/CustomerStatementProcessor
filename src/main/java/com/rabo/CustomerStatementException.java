package com.rabo;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class CustomerStatementException extends RuntimeException {
    public static HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public static String MESSAGE = "Error parsing customer statement";

    public CustomerStatementException() {
        super();
    }

    public CustomerStatementException(String message) {
        super(message);
    }
    public CustomerStatementException(String message, Throwable cause) {
        super(message, cause);
    }
}

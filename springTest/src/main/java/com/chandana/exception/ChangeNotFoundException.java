package com.chandana.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ChangeNotFoundException extends RuntimeException {
    public ChangeNotFoundException(String message){
        super(message);
    }
}

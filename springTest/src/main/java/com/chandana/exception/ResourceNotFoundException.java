package com.chandana.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)  // This annotation is used to change default response that client gets.
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super( message);
    }
}

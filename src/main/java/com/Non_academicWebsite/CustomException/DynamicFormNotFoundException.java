package com.Non_academicWebsite.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DynamicFormNotFoundException extends Exception{
    public DynamicFormNotFoundException(String message){
        super(message);
    }
}

package com.Non_academicWebsite.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FormUnderProcessException extends Exception{
    public FormUnderProcessException(String message){
        super(message);
    }
}

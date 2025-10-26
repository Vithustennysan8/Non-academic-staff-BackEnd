package com.Non_academicWebsite.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceExistsException extends Exception{
    public ResourceExistsException(String mes){
        super(mes);
    }
}

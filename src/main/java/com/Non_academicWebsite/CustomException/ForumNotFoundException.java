package com.Non_academicWebsite.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ForumNotFoundException extends Exception{
    public ForumNotFoundException(String message){
        super(message);
    }
}

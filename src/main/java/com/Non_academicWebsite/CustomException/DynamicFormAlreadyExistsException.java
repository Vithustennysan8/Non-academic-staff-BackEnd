package com.Non_academicWebsite.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DynamicFormAlreadyExistsException extends Throwable {
    public DynamicFormAlreadyExistsException(String s) {
        super(s);
    }
}

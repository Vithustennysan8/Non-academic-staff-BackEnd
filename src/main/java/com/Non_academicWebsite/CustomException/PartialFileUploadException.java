package com.Non_academicWebsite.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.MULTI_STATUS)
public class PartialFileUploadException extends Exception{
    public PartialFileUploadException(String mes){
        super(mes);
    }
}

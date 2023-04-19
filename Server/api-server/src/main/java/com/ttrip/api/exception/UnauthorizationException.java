package com.ttrip.api.exception;


import lombok.Getter;

@Getter
public class UnauthorizationException extends Exception {
    public UnauthorizationException(String message){
        super(message);
    }
}

package com.weatherforcaste.api;

public class BadRequestException extends Exception{
    public BadRequestException(String message){
        super(message);
    }
}

package com.weatherforcast.api;

public class GeologicalException extends RuntimeException{

    public GeologicalException(String message, Throwable cause){
        super(message,cause);
    }


    public  GeologicalException(String message){
        super(message);
    }
}

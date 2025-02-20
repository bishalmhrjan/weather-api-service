package com.weatherforcaste.api.location;

public class LocationNotFoundException extends RuntimeException{
    public LocationNotFoundException(String locationCode){
        super("No Location found with the given code: "+locationCode);
    }

    public LocationNotFoundException(String countryCode, String cityName){
        super("No location found with the given country code: "+countryCode+" and city: "+cityName);
    }
}

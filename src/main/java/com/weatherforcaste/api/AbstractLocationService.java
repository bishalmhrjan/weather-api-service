package com.weatherforcaste.api;

import com.weatherapi.forecast.common.Location;
import com.weatherforcaste.api.location.LocationNotFoundException;
import com.weatherforcaste.api.location.LocationRepository;

public abstract class AbstractLocationService {
    protected LocationRepository locationRepository;

    public Location get(String code){
        Location location=locationRepository.findByCode(code);

        if(location==null){
            throw new LocationNotFoundException(code);
        }
        return location;
    }
}

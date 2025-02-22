package com.weatherforcast.api;

import com.weatherapi.forecast.common.Location;
import com.weatherforcast.api.location.LocationNotFoundException;
import com.weatherforcast.api.location.LocationRepository;

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

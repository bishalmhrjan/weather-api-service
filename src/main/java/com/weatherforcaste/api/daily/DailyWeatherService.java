package com.weatherforcaste.api.daily;

import com.weatherapi.forecast.common.DailyWeather;
import com.weatherapi.forecast.common.Location;
import com.weatherforcaste.api.location.LocationNotFoundException;
import com.weatherforcaste.api.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DailyWeatherService {

    private DailyWeatherRepository dailyWeatherRepository;

    private LocationRepository locationRepository;

    public DailyWeatherService(DailyWeatherRepository dailyWeatherRepository, LocationRepository locationRepository) {
        this.dailyWeatherRepository = dailyWeatherRepository;
        this.locationRepository = locationRepository;
    }

    public List<DailyWeather> getByLocation(Location location){
        String countryCode= location.getCountryCode();
        String cityName=location.getCityName();

        Location locationInDB = locationRepository.findByCountryCodeAndCityName(countryCode,cityName);
        if(location==null){
            throw new LocationNotFoundException(countryCode,cityName);
        }
        return dailyWeatherRepository.findByLocationCode(locationInDB.getCode());
    }

    public List<DailyWeather> updateByLocationCode(String code, List<DailyWeather> dailyWeathersInRequest) throws LocationNotFoundException{
        Location location = locationRepository.findByCode(code);
        if(location==null){
            throw new LocationNotFoundException(code);
        }

        for(DailyWeather data:dailyWeathersInRequest){
            data.getId().setLocation(location);
        }
        List<DailyWeather> dailyWeathersInDB=location.getListDailyWeather();
        List<DailyWeather> dailyWeatherToBeRemoved = new ArrayList<>();

        for(DailyWeather forecast:dailyWeathersInDB){
            if(!dailyWeathersInRequest.contains(forecast)){
                dailyWeatherToBeRemoved.add(forecast.getShallowCopy());
            }
        }

        for(DailyWeather forcastToBeRemoved:dailyWeatherToBeRemoved){
            dailyWeathersInDB.remove(forcastToBeRemoved);
        }
        return (List<DailyWeather>) dailyWeatherRepository.saveAll(dailyWeathersInRequest);
    }
}

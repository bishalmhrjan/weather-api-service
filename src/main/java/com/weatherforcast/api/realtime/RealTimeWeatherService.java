package com.weatherforcast.api.realtime;

import com.weatherapi.forecast.common.Location;
import com.weatherapi.forecast.common.RealTimeWeather;
import com.weatherforcast.api.location.LocationNotFoundException;
import com.weatherforcast.api.location.LocationRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealTimeWeatherService {
    private RealTimeWeatherRepository realTimeWeatherRepository;
    private LocationRepository locationRepository;

    public RealTimeWeather getByLocation(Location location) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        RealTimeWeather RealTimeWeather = realTimeWeatherRepository.findByCountryCodeAndCity(countryCode, cityName);
        if (RealTimeWeather == null) {
            throw new LocationNotFoundException(countryCode, cityName);
        }
        return RealTimeWeather;
    }

    public RealTimeWeather getByLocationCode(String locationCode) {
        RealTimeWeather realTimeWeather = realTimeWeatherRepository.findByLocationCode(locationCode);

        if (realTimeWeather == null) {
            throw new LocationNotFoundException(locationCode);
        }

        return realTimeWeather;
    }

    public RealTimeWeather update(String locationCode, RealTimeWeather realtimeWeather) {
        Location location = locationRepository.findByCode(locationCode);

        if (location == null) {
            throw new LocationNotFoundException(locationCode);
        }


        realtimeWeather.setLocation(location);
        realtimeWeather.setLastUpdated(new Date());

        if (location.getRealtimeWeather() == null) {
            location.setRealtimeWeather(realtimeWeather);
            Location updatedLocation = locationRepository.save(location);

            return updatedLocation.getRealtimeWeather();
        }

        return realTimeWeatherRepository.save(realtimeWeather);
    }
}

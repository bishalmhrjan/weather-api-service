package com.weatherforcast.api.realtime;

import com.weatherapi.forecast.common.RealTimeWeather;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RealTimeWeatherRepository extends CrudRepository<RealTimeWeather,String> {
    @Query("SELECT r FROM RealtimeWeather r WHERE r.location.countryCode = ?1 AND r.location.cityName = ?2")
    public RealTimeWeather findByCountryCodeAndCity(String countryCode, String city);

    @Query("SELECT r FROM RealtimeWeather r WHERE r.id = ?1 AND r.location.trashed = false")
    public RealTimeWeather findByLocationCode(String locationCode);
}

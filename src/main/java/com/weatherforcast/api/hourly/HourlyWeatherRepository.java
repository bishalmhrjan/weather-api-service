package com.weatherforcast.api.hourly;


import com.weatherapi.forecast.common.HourlyWeather;
import com.weatherapi.forecast.common.HourlyWeatherId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HourlyWeatherRepository extends CrudRepository<HourlyWeather, HourlyWeatherId> {
    @Query("""
            SELECT h FROM HourlyWeather h WHERE
            h.id.location.code = ?1 AND h.id.hourOfDay > ?2
            AND h.id.location.trashed = false
            """)
    public List<HourlyWeather> findByLocationCode(String locationCode, int currentHour);
}
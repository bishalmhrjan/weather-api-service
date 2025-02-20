package com.weatherforcaste.api.daily;

import com.weatherapi.forecast.common.DailyWeather;
import com.weatherapi.forecast.common.DailyWeatherId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DailyWeatherRepository extends CrudRepository<DailyWeather, DailyWeatherId> {

    @Query("""
            Select d from DailyWeather d where d.id.location.code=?1 
            AND d.id.location.trashed=false
            """) //JPQL language textblock
// """"""". it removes problem of concerntration.
    public List<DailyWeather> findByLocationCode(String locadtionCode);
}

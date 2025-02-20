package com.weatherforcaste.api.location;

import com.weatherapi.forecast.common.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface FilterableLocationRepository {
    public Page<Location> listWithFilter(Pageable pageable, Map<String, Object> filterFields);

}

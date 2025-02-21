package com.weatherforcast.api.location;

import com.weatherapi.forecast.common.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface LocationRepository extends  FilterableLocationRepository, CrudRepository<Location, String>,
        PagingAndSortingRepository<Location, String> {
    @Query("SELECT l FROM Location l WHERE l.trashed = false")
    @Deprecated
    List<Location> findUntrashed();

    @Query("SELECT l FROM Location l WHERE l.trashed = false")
    @Deprecated
    public Page<Location> findUntrashed(Pageable pageable);

    @Query("SELECT l FROM Location l WHERE l.trashed = false AND l.code = ?1")
    public Location findByCode(String code);

    @Modifying
    @Query("UPDATE Location SET trashed = true WHERE code = ?1")
    public void trashByCode(String code);

    @Query("SELECT l FROM Location l WHERE l.countryCode = ?1 AND l.cityName = ?2 AND l.trashed = false")
    public Location findByCountryCodeAndCityName(String countryCode, String cityName);
}

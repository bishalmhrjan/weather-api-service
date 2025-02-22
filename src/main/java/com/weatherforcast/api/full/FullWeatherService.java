package com.weatherforcast.api.full;

import com.weatherapi.forecast.common.DailyWeather;
import com.weatherapi.forecast.common.HourlyWeather;
import com.weatherapi.forecast.common.Location;
import com.weatherapi.forecast.common.RealTimeWeather;
import com.weatherforcast.api.AbstractLocationService;
import com.weatherforcast.api.location.LocationNotFoundException;
import com.weatherforcast.api.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FullWeatherService extends AbstractLocationService {

	public FullWeatherService(LocationRepository repo) {
		super();
		this.locationRepository = repo;
	}
	
	public Location getByLocation(Location locationFromIP) {
		String cityName = locationFromIP.getCityName();
		String countryCode = locationFromIP.getCountryCode();
		
		Location locationInDB = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);
		
		if (locationInDB == null) {
			throw new LocationNotFoundException(countryCode, cityName);
		}
		
		return locationInDB;
	}
	
	public Location update(String locationCode, Location locationInRequest) {
		Location locationInDB = locationRepository.findByCode(locationCode);
		
		if (locationInDB == null) {
			throw new LocationNotFoundException(locationCode);
		}
		
		setLocationForWeatherData(locationInRequest, locationInDB);
		
		saveRealtimeWeatherIfNotExistBefore(locationInRequest, locationInDB);
		
		locationInRequest.copyAllFieldsFrom(locationInDB);
		
		return locationRepository.save(locationInRequest);
	}

	private void saveRealtimeWeatherIfNotExistBefore(Location locationInRequest, Location locationInDB) {
		if (locationInDB.getRealtimeWeather() == null) {
			locationInDB.setRealtimeWeather(locationInRequest.getRealtimeWeather());
			locationRepository.save(locationInDB);
		}
	}

	private void setLocationForWeatherData(Location locationInRequest, Location locationInDB) {
		RealTimeWeather realtimeWeather = locationInRequest.getRealtimeWeather();
		realtimeWeather.setLocation(locationInDB);
		realtimeWeather.setLastUpdated(new Date());
		
		List<DailyWeather> listDailyWeather = locationInRequest.getListDailyWeather();
		listDailyWeather.forEach(dw -> dw.getId().setLocation(locationInDB));
		
		List<HourlyWeather> listHourlyWeather = locationInRequest.getListHourlyWeather();
		listHourlyWeather.forEach(hw -> hw.getId().setLocation(locationInDB));
	}
}

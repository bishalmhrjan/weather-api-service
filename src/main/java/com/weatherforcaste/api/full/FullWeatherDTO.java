package com.weatherforcaste.api.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.weatherforcaste.api.daily.DailyWeatherDTO;
import com.weatherforcaste.api.hourly.HourlyWeatherDTO;
import com.weatherforcaste.api.realtime.RealTimeWeatherDTO;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

public class FullWeatherDTO {
	private String location;
	
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = RealtimeWeatherFieldFilter.class)
	@Valid
	private RealTimeWeatherDTO realtimeWeather = new RealTimeWeatherDTO();
	
	@JsonProperty("hourly_forecast")
	@Valid
	private List<HourlyWeatherDTO> listHourlyWeather = new ArrayList<>();
	
	@JsonProperty("daily_forecast")
	@Valid
	private List<DailyWeatherDTO> listDailyWeather = new ArrayList<>();

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public RealTimeWeatherDTO getRealtimeWeather() {
		return realtimeWeather;
	}

	public void setRealtimeWeather(RealTimeWeatherDTO realtimeWeather) {
		this.realtimeWeather = realtimeWeather;
	}

	public List<HourlyWeatherDTO> getListHourlyWeather() {
		return listHourlyWeather;
	}

	public void setListHourlyWeather(List<HourlyWeatherDTO> listHourlyWeather) {
		this.listHourlyWeather = listHourlyWeather;
	}

	public List<DailyWeatherDTO> getListDailyWeather() {
		return listDailyWeather;
	}

	public void setListDailyWeather(List<DailyWeatherDTO> listDailyWeather) {
		this.listDailyWeather = listDailyWeather;
	}


	
	
}

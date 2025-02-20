package com.weatherforcaste.api.full;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.weatherforcaste.api.daily.DailyWeatherDTO;
import com.weatherforcaste.api.hourly.HourlyWeatherDTO;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

public class FullweatherDTO {
    private String location;
/*
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = RealtimeWeatherFieldFilter.class)
    @Valid
    private RealtimeWeatherDTO realtimeWeather = new RealtimeWeatherDTO();
*/
    @JsonProperty("hourly_forecast")
    @Valid
    private List<HourlyWeatherDTO> listHourlyWeather = new ArrayList<>();

    @JsonProperty("daily_forecast")
    @Valid
    private List<DailyWeatherDTO> listDailyWeather = new ArrayList<>();
}

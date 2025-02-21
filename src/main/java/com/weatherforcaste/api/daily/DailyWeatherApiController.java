package com.weatherforcaste.api.daily;

import com.weatherapi.forecast.common.Location;
import com.weatherforcaste.api.CommonUtility;
import com.weatherforcaste.api.GeolocationException;
import com.weatherforcaste.api.GeolocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/daily")
@Validated
public class DailyWeatherApiController {

    private DailyWeatherService dailyWeatherService;
    private GeolocationService  geolocationService;
    private ModelMapper modelMapper;

    public DailyWeatherApiController(DailyWeatherService dailyWeatherService, GeolocationService geolocationService, ModelMapper modelMapper) {
        this.dailyWeatherService = dailyWeatherService;
        this.geolocationService = geolocationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> listDailyForecastByIpAddress(HttpServletRequest request) throws GeolocationException {
        String ipAddress = CommonUtility.getIPAddress(request);
        Location locationFromIp =geolocationService.getLocation(ipAddress);
        return null;
    }

}

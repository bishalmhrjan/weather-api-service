package com.weatherforcast.api.base;

import com.weatherforcast.api.GeolocationException;
import com.weatherforcast.api.daily.DailyWeatherApiController;
import com.weatherforcast.api.full.FullWeatherApiController;
import com.weatherforcast.api.hourly.HourlyWeatherApiController;
import com.weatherforcast.api.location.LocationAPIController;
import com.weatherforcast.api.realtime.RealtimeWeatherApiController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class MainController {

    @GetMapping("/")
    public ResponseEntity<RootEntity> handleBaseURI() throws GeolocationException {
        return ResponseEntity.ok(createRootEntity());
    }

    private RootEntity createRootEntity() throws GeolocationException {
        RootEntity entity = new RootEntity();

        String locationsUrl = linkTo(methodOn(LocationAPIController.class).listLocations()).toString();
        entity.setLocationsUrl(locationsUrl);

        String locationByCodeUrl = linkTo(methodOn(LocationAPIController.class).getLocation(null)).toString();
        entity.setLocationByCodeUrl(locationByCodeUrl);

        String realtimeWeatherByIpUrl = linkTo(
                methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByIPAddress(null)).toString();
        entity.setRealtimeWeatherByIpUrl(realtimeWeatherByIpUrl);

        String realtimeWeatherByCodeUrl = linkTo(
                methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByLocationCode(null)).toString();
        entity.setRealtimeWeatherByCodeUrl(realtimeWeatherByCodeUrl);

        String hourlyForecastByIpUrl = linkTo(
                methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null)).toString();
        entity.setHourlyForecastByIpUrl(hourlyForecastByIpUrl);

        String hourlyForecastByCodeUrl = linkTo(
                methodOn(HourlyWeatherApiController.class).listHourlyForecastByLocationCode(null, null)).toString();
        entity.setHourlyForecastByCodeUrl(hourlyForecastByCodeUrl);

        String dailyForecastByIpUrl = linkTo(
                methodOn(DailyWeatherApiController.class).listDailyForecastByIPAddress(null)).toString();
        entity.setDailyForecastByIpUrl(dailyForecastByIpUrl);

        String dailyForecastByCodeUrl = linkTo(
                methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(null)).toString();
        entity.setDailyForecastByCodeUrl(dailyForecastByCodeUrl);

        String fullWeatherByIpUrl = linkTo(
                methodOn(FullWeatherApiController.class).getFullWeatherByIPAddress(null)).toString();
        entity.setFullWeatherByIpUrl(fullWeatherByIpUrl);

        String fullWeatherByCodeUrl = linkTo(
                methodOn(FullWeatherApiController.class).getFullWeatherByLocationCode(null)).toString();
        entity.setFullWeatherByCodeUrl(fullWeatherByCodeUrl);

        return entity;
    }
}

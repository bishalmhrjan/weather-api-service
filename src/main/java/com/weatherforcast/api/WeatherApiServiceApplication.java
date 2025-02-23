package com.weatherforcast.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.weatherapi.forecast.common.DailyWeather;
import com.weatherapi.forecast.common.HourlyWeather;
import com.weatherapi.forecast.common.Location;
import com.weatherapi.forecast.common.RealTimeWeather;
import com.weatherforcast.api.daily.DailyWeatherDTO;
import com.weatherforcast.api.full.FullWeatherDTO;
import com.weatherforcast.api.hourly.HourlyWeatherDTO;
import com.weatherforcast.api.realtime.RealTimeWeatherDTO;
import com.weatherforcast.api.security.RsaKeyProperties;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class WeatherApiServiceApplication {
    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        configureMappingForHourlyWeather(mapper);

        configureMappingForDailyWeather(mapper);

        configureMappingForFullWeather(mapper);

        configureMappingForRealtimeWeather(mapper);

        return mapper;
    }

    private void configureMappingForRealtimeWeather(ModelMapper mapper) {
        mapper.typeMap(RealTimeWeatherDTO.class, RealTimeWeather.class)
                .addMappings(m -> m.skip(RealTimeWeather::setLocation));
    }

    private void configureMappingForFullWeather(ModelMapper mapper) {
        mapper.typeMap(Location.class, FullWeatherDTO.class)
                .addMapping(src -> src.toString(), FullWeatherDTO::setLocation);
    }

    private void configureMappingForDailyWeather(ModelMapper mapper) {
        mapper.typeMap(DailyWeather.class, DailyWeatherDTO.class)
                .addMapping(src -> src.getId().getDayOfMonth(), DailyWeatherDTO::setDayOfMonth)
                .addMapping(src -> src.getId().getMonth(), DailyWeatherDTO::setMonth);

        mapper.typeMap(DailyWeatherDTO.class, DailyWeather.class)
                .addMapping(src -> src.getDayOfMonth(),
                        (dest, value) -> dest.getId().setDayOfMonth(value != null ? (int) value : 0))

                .addMapping(src -> src.getMonth(),
                        (dest, value) -> dest.getId().setMonth(value != null ? (int) value : 0));
    }

    private void configureMappingForHourlyWeather(ModelMapper mapper) {
        mapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class)
                .addMapping(src -> src.getId().getHourOfDay(), HourlyWeatherDTO::setHourOfDay);

        mapper.typeMap(HourlyWeatherDTO.class, HourlyWeather.class)
                .addMapping(src -> src.getHourOfDay(),
                        (dest, value) ->	dest.getId().setHourOfDay(value != null ? (int) value : 0));
    }


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        return objectMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiServiceApplication.class, args);
    }
}

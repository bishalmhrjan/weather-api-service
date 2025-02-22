package com.weatherforcast.api;

import com.weatherforcast.api.security.RsaKeyProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class WeatherApiServiceApplication {
}

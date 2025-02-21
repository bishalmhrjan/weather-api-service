package com.weatherforcaste.api.daily;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyWeatherListDTO {
    private String location;

    private List<DailyWeatherDTO> dailyForecast = new ArrayList<>();
    public void addDailyWeatherDTO(DailyWeatherDTO dto) {
        this.dailyForecast.add(dto);
    }

}

package com.weatherforcast.api.full;


import com.weatherforcast.api.realtime.RealTimeWeatherDTO;

public class RealtimeWeatherFieldFilter {
	
	public boolean equals(Object object) {
		
		if (object instanceof RealTimeWeatherDTO) {
			RealTimeWeatherDTO dto = (RealTimeWeatherDTO) object;
			return dto.getStatus() == null;
		}
		
		return false;
	}
}

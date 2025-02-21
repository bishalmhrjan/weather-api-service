package com.weatherforcaste.api;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import com.weatherapi.forecast.common.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
//@EnableConfigurationProperties(RsaKeyProperties.class)
public class GeolocationService {


private static final Logger LOGGER = LoggerFactory.getLogger(GeolocationService.class);
private String DBPATH ="/ip2locdb/IP2LOCATION-LITE-DB3.BIN";
private IP2Location ip2Location = new IP2Location();

public GeolocationService(){
    try{
        InputStream inputStream = getClass().getResourceAsStream(DBPATH);
        byte[] data = inputStream.readAllBytes();
        ip2Location.Open(data);
        inputStream.close();
    } catch (IOException e) {
       LOGGER.error(e.getMessage(),e);
    }
}
public Location getLocation(String ipAddress) throws GeolocationException{
try{
    IPResult result =ip2Location.IPQuery(ipAddress);
    if(!"OK".equals(result.getStatus())){
        throw new GeolocationException("Geolocation failed with status: "+result.getStatus());
    }
    LOGGER.info(result.toString());
    return new Location(result.getCity(), result.getRegion(), result.getCountryLong(), result.getCountryShort());
}catch (IOException e){
    throw  new GeolocationException("Error querying IP database", e);
}
}

}

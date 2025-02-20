package com.weatherforcaste.api;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtility {
    private static Logger LOGGER= LoggerFactory.getLogger(CommonUtility.class);

    public static String getIPAddress(HttpServletRequest request){
    String ip= request.getHeader("X-Forwarded-For");
    if(ip==null||ip.isEmpty()){
        ip=request.getRemoteAddr();
    }

    LOGGER.info("Clients IP Address: "+ip);
    return ip;
    }
}

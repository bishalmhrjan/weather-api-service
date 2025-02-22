package com.weatherforcast.api.location;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/locations")
@Validated
public class LocationAPIController {

    private LocationService service;

    private ModelMapper modelMapper;
}

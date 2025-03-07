package com.weatherforcast.api.full;

import com.weatherapi.forecast.common.Location;
import com.weatherforcast.api.BadRequestException;
import com.weatherforcast.api.CommonUtility;
import com.weatherforcast.api.GeolocationException;
import com.weatherforcast.api.GeolocationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/full")
public class FullWeatherApiController {

	private GeolocationService locationService;
	private FullWeatherService weatherService;
	private ModelMapper modelMapper;
	private FullWeatherModelAssembler modelAssembler;
	
	public FullWeatherApiController(GeolocationService locationService, FullWeatherService weatherService,
			ModelMapper modelMapper, FullWeatherModelAssembler modelAssembler) {
		super();
		this.locationService = locationService;
		this.weatherService = weatherService;
		this.modelMapper = modelMapper;
		this.modelAssembler = modelAssembler;
	}

	@GetMapping
	public ResponseEntity<?> getFullWeatherByIPAddress(HttpServletRequest request) throws GeolocationException {
		String ipAddress = CommonUtility.getIPAddress(request);
		
		Location locationFromIP = locationService.getLocation(ipAddress);
		Location locationInDB = weatherService.getByLocation(locationFromIP);
		
		FullWeatherDTO dto = entity2DTO(locationInDB);
		
		return ResponseEntity.ok(modelAssembler.toModel(dto));
	}
	
	@GetMapping("/{locationCode}")
	public ResponseEntity<?> getFullWeatherByLocationCode(@PathVariable String locationCode) {
		
		Location locationInDB = weatherService.get(locationCode);
		
		FullWeatherDTO dto = entity2DTO(locationInDB);
		
		return ResponseEntity.ok(addLinksByLocation(dto, locationCode));
	}	
	
	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateFullWeather(@PathVariable String locationCode, 
			@RequestBody @Valid FullWeatherDTO dto) throws BadRequestException {
		
		if (dto.getListHourlyWeather().isEmpty()) {
			throw new BadRequestException("Hourly weather data cannot be empty");
		}
		
		if (dto.getListDailyWeather().isEmpty()) {
			throw new BadRequestException("Daily weather data cannot be empty");
		}
		
		Location locationInRequest = dto2Entity(dto);
		
		Location updatedLocation = weatherService.update(locationCode, locationInRequest);
		
		FullWeatherDTO updatedDto = entity2DTO(updatedLocation);
		
		return ResponseEntity.ok(addLinksByLocation(updatedDto, locationCode));
	}
	
	private FullWeatherDTO entity2DTO(Location entity) {
		FullWeatherDTO dto = modelMapper.map(entity, FullWeatherDTO.class);
		
		// do not show the field location in realtime_weather object
		dto.getRealtimeWeather().setLocation(null);
		return dto;
	}
	
	private Location dto2Entity(FullWeatherDTO dto) {
		return modelMapper.map(dto, Location.class);
	}
	
	private EntityModel<FullWeatherDTO> addLinksByLocation(FullWeatherDTO dto, String locationCode) {
		return EntityModel.of(dto)
				.add(linkTo(
						methodOn(FullWeatherApiController.class).getFullWeatherByLocationCode(locationCode))
							.withSelfRel());		
	}
}

package com.weatherforcaste.api.full;

import com.weatherforcaste.api.GeolocationException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FullWeatherModelAssembler 
	implements RepresentationModelAssembler<FullWeatherDTO, EntityModel<FullWeatherDTO>> {

	@Override
	public EntityModel<FullWeatherDTO> toModel(FullWeatherDTO dto) {
		EntityModel<FullWeatherDTO> entityModel = EntityModel.of(dto);

		try {
			entityModel.add(linkTo(
					methodOn(FullWeatherApiController.class).getFullWeatherByIPAddress(null))
						.withSelfRel());
		} catch (GeolocationException e) {
			throw new RuntimeException(e);
		}

		return entityModel;
	}

}

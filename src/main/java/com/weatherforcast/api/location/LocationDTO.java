package com.weatherforcast.api.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.CollectionModel;


@JsonPropertyOrder({"code", "city_name", "region_name", "country_code", "country_name", "enabled"})
@Data
public class LocationDTO  extends CollectionModel {
    @NotNull(message = "Location code cannot be null")
    @Length(min = 3, max = 12, message = "Location code must have 3-12 characters")
    private String code;

    @NotNull(message = "City name cannot be null")
    @Length(min = 3, max = 128, message = "City name must have 3-128 characters")
    private String cityName;

    @Length(min = 3, max = 128, message = "Region name must have 3-128 characters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String regionName;

    @NotNull(message = "Country name cannot be null")
    @Length(min = 3, max = 64, message = "Country name must have 3-64 characters")
    private String countryName;

    @NotNull(message = "Country code cannot be null")
    @Length(min = 2, max = 2, message = "Country code must have 2 characters")
    private String countryCode;
    private boolean enabled;

}

package com.weatherforcast.api.location;

import com.weatherapi.forecast.common.Location;
import com.weatherforcast.api.BadRequestException;
import com.weatherforcast.api.daily.DailyWeatherApiController;
import com.weatherforcast.api.full.FullWeatherApiController;
import com.weatherforcast.api.hourly.HourlyWeatherApiController;
import com.weatherforcast.api.realtime.RealtimeWeatherApiController;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/locations")
@Validated
public class LocationAPIController {
    private LocationService service;
    private ModelMapper modelMapper;

    private Map<String, String> propertyMap = Map.of(
            "code", "code",
            "city_name", "cityName",
            "region_name", "regionName",
            "country_code", "countryCode",
            "country_name", "countryName",
            "enabled", "enabled"
    );


    @PostMapping
    public ResponseEntity<LocationDTO> addLocation(@RequestBody @Valid LocationDTO dto) {
        Location addedLocation = service.add(dto2Entity(dto));
        URI uri = URI.create("/v1/locations/" + addedLocation.getCode());

        return ResponseEntity.created(uri).body(addLinks2Item(entity2DTO(addedLocation)));
    }

    @Deprecated
    public ResponseEntity<?> listLocations() {
        List<Location> locations = service.list();

        if (locations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listEntity2ListDTO(locations));

    }

    @GetMapping
    public ResponseEntity<?> listLocations(
            @RequestParam(value = "page", required = false, defaultValue = "1")
            @Min(value = 1)	Integer pageNum,

            @RequestParam(value = "size", required = false, defaultValue = "5")
            @Min(value = 5) @Max(value = 20) Integer pageSize,

            @RequestParam(value = "sort", required = false, defaultValue = "code") String sortOption,

            @RequestParam(value = "enabled", required = false, defaultValue = "") String enabled,

            @RequestParam(value = "region_name", required = false, defaultValue = "") String regionName,

            @RequestParam(value = "country_code", required = false, defaultValue = "") String countryCode

    ) throws BadRequestException {

        sortOption = validateSortOption(sortOption);

        Map<String, Object> filterFields = getFilterFields(enabled, regionName, countryCode);

        Page<Location> page = service.listByPage(pageNum - 1, pageSize, sortOption, filterFields);

        List<Location> locations = page.getContent();

        if (locations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(addPageMetadataAndLinks2Collection(
                listEntity2ListDTO(locations), page, sortOption, enabled, regionName, countryCode));
    }

    private Map<String, Object> getFilterFields(String enabled, String regionName, String countryCode) {
        Map<String, Object> filterFields = new HashMap<>();

        if (!"".equals(enabled)) {
            filterFields.put("enabled", Boolean.parseBoolean(enabled));
        }

        if (!"".equals(regionName)) {
            filterFields.put("regionName", regionName);
        }

        if (!"".equals(countryCode)) {
            filterFields.put("countryCode", countryCode);
        }
        return filterFields;
    }

    private String validateSortOption(String sortOption) throws BadRequestException {
        String translatedSortOption = sortOption;

        String[] sortFields = sortOption.split(",");


        if (sortFields.length > 1) { // sorted by multiple fields

            for (int i = 0; i < sortFields.length; i++) {
                String actualFieldName = sortFields[i].replace("-", "");

                if (!propertyMap.containsKey(actualFieldName)) {
                    throw new BadRequestException("invalid sort field: " + actualFieldName);
                }

                translatedSortOption = translatedSortOption.replace(actualFieldName, propertyMap.get(actualFieldName));
            }

        } else { // sorted by a single field
            String actualFieldName = sortOption.replace("-", "");
            if (!propertyMap.containsKey(actualFieldName)) {
                throw new BadRequestException("invalid sort field: " + actualFieldName);
            }

            translatedSortOption = translatedSortOption.replace(actualFieldName, propertyMap.get(actualFieldName));
        }
        return translatedSortOption;
    }

    private CollectionModel<LocationDTO> addPageMetadataAndLinks2Collection(
            List<LocationDTO> listDTO, Page<Location> pageInfo, String sortField,
            String enabled, String regionName, String countryCode) throws BadRequestException {

        String actualEnabled = "".equals(enabled) ? null : enabled;
        String actualRegionName = "".equals(regionName) ? null : regionName;
        String actualCountryCode = "".equals(countryCode) ? null : countryCode;


        // add self link to each individual item
        for (LocationDTO dto : listDTO) {
            dto.add(linkTo(methodOn(LocationAPIController.class).getLocation(dto.getCode())).withSelfRel());
        }

        int pageSize = pageInfo.getSize();
        int pageNum = pageInfo.getNumber() + 1;
        long totalElements = pageInfo.getTotalElements();
        int totalPages = pageInfo.getTotalPages();

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pageSize, pageNum, totalElements);

        CollectionModel<LocationDTO> collectionModel = PagedModel.of(listDTO, pageMetadata);

        // add self link to collection
        collectionModel.add(linkTo(methodOn(LocationAPIController.class)
                .listLocations(pageNum, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode))
                .withSelfRel());

        if (pageNum > 1) {
            // add link to first page if the current page is not the first one
            collectionModel.add(
                    linkTo(methodOn(LocationAPIController.class)
                            .listLocations(1, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode))
                            .withRel(IanaLinkRelations.FIRST));

            // add link to the previous page if the current page is not the first one
            collectionModel.add(
                    linkTo(methodOn(LocationAPIController.class)
                            .listLocations(pageNum - 1, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode))
                            .withRel(IanaLinkRelations.PREV));
        }

        if (pageNum < totalPages) {
            // add link to next page if the current page is not the last one
            collectionModel.add(
                    linkTo(methodOn(LocationAPIController.class)
                            .listLocations(pageNum + 1, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode))
                            .withRel(IanaLinkRelations.NEXT));

            // add link to last page if the current page is not the last one
            collectionModel.add(
                    linkTo(methodOn(LocationAPIController.class)
                            .listLocations(totalPages, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode))
                            .withRel(IanaLinkRelations.LAST));
        }


        return collectionModel;

    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getLocation(@PathVariable("code") String code) {
        Location location = service.get(code);

        return ResponseEntity.ok(addLinks2Item(entity2DTO(location)));
    }

    @PutMapping
    public ResponseEntity<?> updateLocation(@RequestBody @Valid LocationDTO dto) {
        Location updatedLocation = service.update(dto2Entity(dto));

        return ResponseEntity.ok(addLinks2Item(entity2DTO(updatedLocation)));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteLocation(@PathVariable("code") String code) {
        service.delete(code);

        return ResponseEntity.noContent().build();
    }

    private List<LocationDTO> listEntity2ListDTO(List<Location> listEntity) {

        return listEntity.stream().map(entity -> entity2DTO(entity))
                .collect(Collectors.toList());

    }

    private LocationDTO entity2DTO(Location entity) {
        return modelMapper.map(entity, LocationDTO.class);
    }

    private Location dto2Entity(LocationDTO dto) {
        return modelMapper.map(dto, Location.class);
    }

    private LocationDTO addLinks2Item(LocationDTO dto) {

        dto.add(linkTo(
                methodOn(LocationAPIController.class).getLocation(dto.getCode()))
                .withSelfRel());

        dto.add(linkTo(
                methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByLocationCode(dto.getCode()))
                .withRel("realtime_weather"));

        dto.add(linkTo(
                methodOn(HourlyWeatherApiController.class).listHourlyForecastByLocationCode(dto.getCode(), null))
                .withRel("hourly_forecast"));

        dto.add(linkTo(
                methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(dto.getCode()))
                .withRel("daily_forecast"));

        dto.add(linkTo(
                methodOn(FullWeatherApiController.class).getFullWeatherByLocationCode(dto.getCode()))
                .withRel("full_forecast"));

        return dto;
    }
}

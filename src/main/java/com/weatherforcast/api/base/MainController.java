package com.weatherforcast.api.base;

import com.weatherforcast.api.location.LocationAPIController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class MainController {

    @GetMapping("/")
    public ResponseEntity<RootEntity> handBaseURI(){
        return ResponseEntity.ok(createRootEntity());
    }

    private RootEntity createRootEntity() {
        RootEntity entity = new RootEntity();

        String locationsUrl=linkTo(methodOn(LocationAPIController.class).listLocations()).toString();

        return null;
    }
}

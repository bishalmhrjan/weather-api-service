package com.weatherforcaste.api.base;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public ResponseEntity<RootEntity> handBaseURI(){
        return ResponseEntity.ok(createRootEntity());
    }

    private RootEntity createRootEntity() {
        RootEntity entity = new RootEntity();

      //  String locationsUrl=linkTo(methodOn(LocationApiController.class))

        return null;
    }
}

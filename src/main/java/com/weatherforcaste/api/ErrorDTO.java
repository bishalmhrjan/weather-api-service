package com.weatherforcaste.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {
    private Date timestamp;
    private int status;
    private String path;
    private List<String> errors = new ArrayList<>();
    public void addError(String message) {
        this.errors.add(message);
    }

}

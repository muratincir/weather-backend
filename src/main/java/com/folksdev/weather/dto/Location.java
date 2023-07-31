package com.folksdev.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Location {
    private String name;
    private String country;
    private String region;
    @JsonProperty("localtime") // localtime alanına eşit localTime (x diyip de ona da eşitlenebilir)
    private String localTime;

}

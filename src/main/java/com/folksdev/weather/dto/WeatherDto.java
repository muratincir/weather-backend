package com.folksdev.weather.dto;

import com.folksdev.weather.model.WeatherEntity;
import lombok.Data;

@Data
public class WeatherDto {
    private String cityName;
    private String country;
    private Integer temperature;

    public WeatherDto(String cityName, String country, Integer temperature) {
    }

    public static WeatherDto convert(WeatherEntity from){
        return new WeatherDto(from.getCityName(),from.getCountry(),from.getTemperature());
    }
}

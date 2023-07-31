package com.folksdev.weather.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String requestedCityName;
    private String cityName;
    private String country;
    private Integer temperature; // Sıcaklık
    private LocalDateTime updatedTime;
    private LocalDateTime responseLocalTime;

    public WeatherEntity(String city, String name, String country, Integer temperature, LocalDateTime now, LocalDateTime parse) {
    }
}

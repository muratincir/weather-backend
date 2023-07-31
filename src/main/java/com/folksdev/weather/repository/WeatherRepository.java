package com.folksdev.weather.repository;

import com.folksdev.weather.model.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<WeatherEntity,String> {

    // Select * from entitiy where requestedCityName order by updateTime desc limit 1
    // En son atılan kaydı bulup o kaydın ne zaman atıldığını bulmak lazım
    Optional<WeatherEntity> findFirstByRequestedCityNameOrderByUpdatedTimeDesc(String city);

}

package com.folksdev.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.folksdev.weather.constants.Constants;
import com.folksdev.weather.dto.WeatherDto;
import com.folksdev.weather.dto.WeatherResponse;
import com.folksdev.weather.model.WeatherEntity;
import com.folksdev.weather.repository.WeatherRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"weathers"})
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Json'ı nesneye çevirmek için kullanıyoruz!!!

    public WeatherService(WeatherRepository weatherRepository, RestTemplate restTemplate) {
        this.weatherRepository = weatherRepository;
        this.restTemplate = restTemplate;
    }
    @Cacheable(key = "#city")
    public WeatherDto getWeatherByCityName(String city){
        Optional<WeatherEntity> weatherEntityOptional = weatherRepository.findFirstByRequestedCityNameOrderByUpdatedTimeDesc(city);


        return weatherEntityOptional.map(weather -> {
            if (weather.getUpdatedTime().isBefore(LocalDateTime.now().minusMinutes(30))){
                return WeatherDto.convert(getWeatherFromWeatherStack(city));
            }
            return WeatherDto.convert(weather);
        }).orElseGet(()->WeatherDto.convert(getWeatherFromWeatherStack(city)));

        // Üst taraf daha doğru kullanım !!!

        /*if(!weatherEntityOptional.isPresent()){
            // verilen city'e ait bilgi yok ise
            return WeatherDto.convert(getWeatherFromWeatherStack(city));
        }


        if(weatherEntityOptional.get().getUpdatedTime().isBefore(LocalDateTime.now().minusMinutes(30))){
            // 30 dk dan önce sorgulanmış ise tekrar sorgulama yap(kendini güncelle)
            return WeatherDto.convert(getWeatherFromWeatherStack(city));
        }

        return WeatherDto.convert(weatherEntityOptional.get());*/



    }


    // Sadece bu service'de erişilecek sadece burada kullanılacaklar !!!


    // Bütün cache'deki verileri sil (belirli bir periyot ile temizle)
    @CacheEvict(allEntries = true)
    @PostConstruct
    @Scheduled(fixedRateString = "30 * 60 * 1000") // 30 dk da bir günceller
    public void clearCache(){}

    private WeatherEntity getWeatherFromWeatherStack(String city){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getWeatherStackUrl(city),String.class);
        try {
            WeatherResponse weatherResponse = objectMapper.readValue(responseEntity.getBody(),WeatherResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return saveWeatherEntity(city,new WeatherResponse());
    }
    private WeatherEntity saveWeatherEntity(String city,WeatherResponse weatherResponse){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        WeatherEntity weatherEntity = new WeatherEntity(city,
                weatherResponse.getLocation().getName(),
                weatherResponse.getLocation().getCountry(),
                weatherResponse.getCurrent().getTemperature(), LocalDateTime.now(),
                LocalDateTime.parse(weatherResponse.getLocation().getLocalTime(),dateTimeFormatter)
        );
        return weatherRepository.save(weatherEntity);
    }

    // API_URL Oluşturma (CONSTANTS -> application.yaml)

    private String getWeatherStackUrl(String city){
        return Constants.API_URL+Constants.ACCESS_KEY_PARAM+Constants.API_KEY+Constants.QUERY_KEY_PARAM+city;
    }



}

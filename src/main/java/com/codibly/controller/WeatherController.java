package com.codibly.controller;

import com.codibly.dtos.WeatherForecastDTO;
import com.codibly.dtos.WeatherSummaryDTO;
import com.codibly.services.WeatherService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/weather")
@RequiredArgsConstructor
@Validated
@RestController
public class WeatherController {

    private static final long MIN_LATITUDE = -90L;
    private static final long MAX_LATITUDE = 90L;
    private static final long MIN_LONGITUDE = -180L;
    private static final long MAX_LONGITUDE = 180L;
    private static final String NOT_NULL_MESSAGE = " must not be null";
    private static final String MIN_LATITUDE_MESSAGE = "Latitude must be >= " + MIN_LATITUDE;
    private static final String MAX_LATITUDE_MESSAGE = "Latitude must be <= " + MAX_LATITUDE;
    private static final String MIN_LONGITUDE_MESSAGE = "Longitude must be >= " + MIN_LONGITUDE;
    private static final String MAX_LONGITUDE_MESSAGE = "Longitude must be <= " + MAX_LONGITUDE;

    private final WeatherService weatherService;

    @GetMapping("/forecast")
    public ResponseEntity<List<WeatherForecastDTO>> getWeeklyForecast(
            @RequestParam @NotNull(message = "Latitude" + NOT_NULL_MESSAGE)
            @Min(value = MIN_LATITUDE, message = MIN_LATITUDE_MESSAGE)
            @Max(value = MAX_LATITUDE, message = MAX_LATITUDE_MESSAGE)
            Double latitude,
            @RequestParam @NotNull(message = "Longitude" + NOT_NULL_MESSAGE)
            @Min(value = MIN_LONGITUDE, message = MIN_LONGITUDE_MESSAGE)
            @Max(value = MAX_LONGITUDE, message = MAX_LONGITUDE_MESSAGE)
            Double longitude) {

        List<WeatherForecastDTO> forecast = weatherService.getWeeklyForecast(latitude, longitude);

        return ResponseEntity.ok(forecast);
    }

    @GetMapping("/summary")
    public WeatherSummaryDTO getWeeklySummary(
            @RequestParam @NotNull(message = "Latitude" + NOT_NULL_MESSAGE)
            @Min(value = MIN_LATITUDE, message = MIN_LATITUDE_MESSAGE)
            @Max(value = MAX_LATITUDE, message = MAX_LATITUDE_MESSAGE)
            Double latitude,
            @RequestParam @NotNull(message = "Longitude" + NOT_NULL_MESSAGE)
            @Min(value = MIN_LONGITUDE, message = MIN_LONGITUDE_MESSAGE)
            @Max(value = MAX_LONGITUDE, message = MAX_LONGITUDE_MESSAGE)
            Double longitude) {

        return weatherService.getWeeklySummary(latitude, longitude);
    }
}

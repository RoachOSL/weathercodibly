package com.codibly.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class WeatherSummaryDTO {

    private Double avgPressureHPa;
    private Double avgSunshineDurationSeconds;
    private Double lowestTemperatureCelsius;
    private Double highestTemperatureCelsius;
    private String forecastSummary;
}

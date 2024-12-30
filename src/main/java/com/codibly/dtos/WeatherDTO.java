package com.codibly.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
public class WeatherDTO {

    private Double latitude;
    private Double longitude;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private List<Integer> weatherCodes;
    private Double maxTemp;
    private Double minTemp;
    private Double sunshineDuration;
    private List<Double> dailyPressure;
}

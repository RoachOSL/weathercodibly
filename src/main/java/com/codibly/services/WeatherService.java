package com.codibly.services;

import com.codibly.connectivity.WeatherAPIHandler;
import com.codibly.dtos.WeatherDTO;
import com.codibly.dtos.WeatherForecastDTO;
import com.codibly.dtos.WeatherSummaryDTO;
import com.codibly.mappers.WeatherDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class WeatherService {

    private static final int FORECAST_DURATION_DAYS = 7;
    private static final int FORECAST_DURATION_WITHOUT_TODAY = FORECAST_DURATION_DAYS - 1;
    private final WeatherAPIHandler weatherAPIHandler;
    private final WeatherDTOMapper weatherDTOMapper;

    public List<WeatherForecastDTO> getWeeklyForecast(Double latitude, Double longitude) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(FORECAST_DURATION_WITHOUT_TODAY);

        List<WeatherDTO> weatherDataList = weatherAPIHandler.fetchWeatherForDateRange(
                latitude, longitude, today, endDate);

        return weatherDataList.stream()
                .map(weatherDTOMapper::toForecastDTO)
                .toList();
    }

    public WeatherSummaryDTO getWeeklySummary(Double latitude, Double longitude) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(FORECAST_DURATION_WITHOUT_TODAY);

        List<WeatherDTO> weatherDataList = weatherAPIHandler.fetchWeatherForDateRange(
                latitude, longitude, today, endDate);

        return weatherDTOMapper.toSummaryDTO(weatherDataList);
    }
}

package com.codibly.connectivity;

import com.codibly.dtos.WeatherDTO;
import com.codibly.mappers.WeatherJacksonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class WeatherAPIHandler {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();
    private static final String WEATHER_API_URL = "https://api.open-meteo.com/v1/forecast?";
    private static final String LATITUDE = "latitude=";
    private static final String LONGITUDE = "&longitude=";
    private static final String START_DATE = "&start_date=";
    private static final String END_DATE = "&end_date=";
    private static final String REQUIRED_PARAMETERS = "&hourly=pressure_msl&daily=weather_code,temperature_2m_max,"
            + "temperature_2m_min,sunshine_duration&timezone=auto";

    private final WeatherJacksonMapper weatherJacksonMapper;

    public Optional<WeatherDTO> fetchOneDayWeather(double latitude, double longitude, LocalDate date) {
        try {
            String geographicCoordinates = LATITUDE + latitude + LONGITUDE + longitude;
            String dateParameter = START_DATE + date + END_DATE + date;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(WEATHER_API_URL + geographicCoordinates + REQUIRED_PARAMETERS + dateParameter))
                    .GET()
                    .build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            return Optional.ofNullable(weatherJacksonMapper.mapWeather(response.body()));
        } catch (IOException | InterruptedException e) {
            log.error("Error occurred while fetching weather data for latitude: {}, longitude: {}, date: {}",
                    latitude, longitude, date, e);
        }

        return Optional.empty();
    }

    public List<WeatherDTO> fetchWeatherForDateRange(double latitude, double longitude,
                                                     LocalDate startDate, LocalDate endDate) {
        List<WeatherDTO> weatherDataList = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            fetchOneDayWeather(latitude, longitude, currentDate).ifPresent(weatherDataList::add);
            currentDate = currentDate.plusDays(1);
        }

        return weatherDataList;
    }
}

package com.codibly.mappers;

import com.codibly.dtos.WeatherDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class WeatherJacksonMapper {

    private static final String LATITUDE_NODE = "latitude";
    private static final String LONGITUDE_NODE = "longitude";
    private static final String DAILY_NODE = "daily";
    private static final String TIME_NODE = "time";
    private static final String WEATHER_CODE_NODE = "weather_code";
    private static final String TEMPERATURE_2M_MAX_NODE = "temperature_2m_max";
    private static final String TEMPERATURE_2M_MIN_NODE = "temperature_2m_min";
    private static final String SUNSHINE_DURATION_NODE = "sunshine_duration";
    private static final String HOURLY_NODE = "hourly";
    private static final String PRESSURE_MSL_NODE = "pressure_msl";
    private static final String JSON_ERROR_MESSAGE = "Error parsing weather JSON";
    private static final int FIRST_ELEMENT = 0;
    private final ObjectMapper objectMapper;

    public WeatherDTO mapWeather(String weatherJson) {
        try {
            JsonNode rootNode = objectMapper.readTree(weatherJson);

            Double latitude = rootNode.path(LATITUDE_NODE).asDouble();
            Double longitude = rootNode.path(LONGITUDE_NODE).asDouble();

            List<String> dateStrings = safeConvertList(
                    rootNode.path(DAILY_NODE).path(TIME_NODE),
                    new TypeReference<List<String>>() {
                    }
            );
            LocalDate date = !dateStrings.isEmpty() ? LocalDate.parse(dateStrings.get(FIRST_ELEMENT)) : null;

            List<Integer> weatherCodes = safeConvertList(
                    rootNode.path(DAILY_NODE).path(WEATHER_CODE_NODE),
                    new TypeReference<List<Integer>>() {
                    }
            );

            List<Double> maxTemps = safeConvertList(
                    rootNode.path(DAILY_NODE).path(TEMPERATURE_2M_MAX_NODE),
                    new TypeReference<List<Double>>() {
                    }
            );
            Double maxTemp = !maxTemps.isEmpty() ? maxTemps.get(FIRST_ELEMENT) : null;

            List<Double> minTemps = safeConvertList(
                    rootNode.path(DAILY_NODE).path(TEMPERATURE_2M_MIN_NODE),
                    new TypeReference<List<Double>>() {
                    }
            );
            Double minTemp = !minTemps.isEmpty() ? minTemps.get(FIRST_ELEMENT) : null;

            List<Double> sunshineDurations = safeConvertList(
                    rootNode.path(DAILY_NODE).path(SUNSHINE_DURATION_NODE),
                    new TypeReference<List<Double>>() {
                    }
            );
            Double sunshineDuration = !sunshineDurations.isEmpty() ? sunshineDurations.get(FIRST_ELEMENT) : null;

            List<Double> pressures = safeConvertList(
                    rootNode.path(HOURLY_NODE).path(PRESSURE_MSL_NODE),
                    new TypeReference<List<Double>>() {
                    }
            );

            return WeatherDTO.builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .date(date)
                    .weatherCodes(weatherCodes)
                    .maxTemp(maxTemp)
                    .minTemp(minTemp)
                    .sunshineDuration(sunshineDuration)
                    .dailyPressure(pressures)
                    .build();

        } catch (JsonProcessingException error) {
            log.error(JSON_ERROR_MESSAGE, error);
            return null;
        }
    }

    private <T> List<T> safeConvertList(JsonNode node, TypeReference<List<T>> typeRef) {
        List<T> list = objectMapper.convertValue(node, typeRef);
        return (list != null) ? list : Collections.emptyList();
    }
}

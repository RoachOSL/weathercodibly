package com.codibly.mappers;

import com.codibly.dtos.WeatherDTO;
import com.codibly.dtos.WeatherForecastDTO;
import com.codibly.dtos.WeatherSummaryDTO;
import com.codibly.enums.WeatherCategory;
import com.codibly.enums.WeatherCode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class WeatherDTOMapper {

    private static final double PANEL_POWER_KW = 2.5;
    private static final double PANEL_EFFICIENCY = 0.2;
    private static final int SECONDS_IN_AN_HOUR = 3600;

    public WeatherForecastDTO toForecastDTO(WeatherDTO weatherDTO) {
        return WeatherForecastDTO.builder()
                .date(weatherDTO.getDate())
                .weatherCodes(weatherDTO.getWeatherCodes())
                .maxTempC(weatherDTO.getMaxTemp())
                .minTempC(weatherDTO.getMinTemp())
                .estimatedEnergyKWh(roundToTwoDecimals(calculateEstimatedEnergy(weatherDTO.getSunshineDuration())))
                .build();
    }

    public WeatherSummaryDTO toSummaryDTO(List<WeatherDTO> weatherDataList) {
        double avgPressure = weatherDataList.stream()
                .flatMap(dto -> dto.getDailyPressure() != null ? dto.getDailyPressure().stream()
                        : Stream.empty())
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        double avgSunshineDuration = weatherDataList.stream()
                .mapToDouble(dto -> dto.getSunshineDuration() != null ? dto.getSunshineDuration() : 0.0)
                .average()
                .orElse(0.0);

        double lowestTemperature = weatherDataList.stream()
                .mapToDouble(dto -> dto.getMinTemp() != null ? dto.getMinTemp() : Double.MAX_VALUE)
                .min()
                .orElse(0.0);

        double highestTemperature = weatherDataList.stream()
                .mapToDouble(dto -> dto.getMaxTemp() != null ? dto.getMaxTemp() : Double.MIN_VALUE)
                .max()
                .orElse(0.0);

        String mostFrequentCondition = getMostFrequentWeatherCategory(weatherDataList);

        return WeatherSummaryDTO.builder()
                .avgPressureHPa(roundToTwoDecimals(avgPressure))
                .avgSunshineDurationSeconds(roundToTwoDecimals(avgSunshineDuration))
                .lowestTemperatureCelsius(lowestTemperature)
                .highestTemperatureCelsius(highestTemperature)
                .forecastSummary(mostFrequentCondition)
                .build();
    }

    private double roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private double calculateEstimatedEnergy(Double sunshineDurationInSeconds) {
        if (sunshineDurationInSeconds == null || sunshineDurationInSeconds <= 0) {
            return 0.0;
        }

        double exposureTimeHours = sunshineDurationInSeconds / SECONDS_IN_AN_HOUR;

        return PANEL_POWER_KW * exposureTimeHours * PANEL_EFFICIENCY;
    }

    private String getMostFrequentWeatherCategory(List<WeatherDTO> weatherDataList) {
        Map<String, Long> categoryFrequency = countWeatherCategoryFrequency(weatherDataList);

        return categoryFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(String.valueOf(WeatherCategory.UNKNOWN));
    }

    private Map<String, Long> countWeatherCategoryFrequency(List<WeatherDTO> weatherDataList) {
        return weatherDataList.stream()
                .flatMap(dto -> dto.getWeatherCodes().stream())
                .map(WeatherCode::getCategoryForCode)
                .map(WeatherCategory::getDescription)
                .collect(Collectors.groupingBy(category -> category, Collectors.counting()));
    }
}

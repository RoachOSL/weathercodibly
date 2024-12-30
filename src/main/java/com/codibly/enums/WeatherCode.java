package com.codibly.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum WeatherCode {

    CLEAR_SKY(0, WeatherCategory.CLEAR_SKY),
    MAINLY_CLEAR(1, WeatherCategory.MAINLY_CLEAR),
    PARTLY_CLOUDY(2, WeatherCategory.PARTLY_CLOUDY),
    OVERCAST(3, WeatherCategory.OVERCAST),
    FOG(45, WeatherCategory.FOG),
    DEPOSITING_RIME_FOG(48, WeatherCategory.FOG),
    DRIZZLE_LIGHT(51, WeatherCategory.DRIZZLE),
    DRIZZLE_MODERATE(53, WeatherCategory.DRIZZLE),
    DRIZZLE_DENSE(55, WeatherCategory.DRIZZLE),
    FREEZING_DRIZZLE_LIGHT(56, WeatherCategory.FREEZING_DRIZZLE),
    FREEZING_DRIZZLE_DENSE(57, WeatherCategory.FREEZING_DRIZZLE),
    RAIN_SLIGHT(61, WeatherCategory.RAIN),
    RAIN_MODERATE(63, WeatherCategory.RAIN),
    RAIN_HEAVY(65, WeatherCategory.RAIN),
    FREEZING_RAIN_LIGHT(66, WeatherCategory.FREEZING_RAIN),
    FREEZING_RAIN_HEAVY(67, WeatherCategory.FREEZING_RAIN),
    SNOW_SLIGHT(71, WeatherCategory.SNOW),
    SNOW_MODERATE(73, WeatherCategory.SNOW),
    SNOW_HEAVY(75, WeatherCategory.SNOW),
    SNOW_GRAINS(77, WeatherCategory.SNOW_GRAINS),
    RAIN_SHOWERS_SLIGHT(80, WeatherCategory.RAIN_SHOWERS),
    RAIN_SHOWERS_MODERATE(81, WeatherCategory.RAIN_SHOWERS),
    RAIN_SHOWERS_VIOLENT(82, WeatherCategory.RAIN_SHOWERS),
    SNOW_SHOWERS_SLIGHT(85, WeatherCategory.SNOW_SHOWERS),
    SNOW_SHOWERS_HEAVY(86, WeatherCategory.SNOW_SHOWERS),
    THUNDERSTORM_SLIGHT(95, WeatherCategory.THUNDERSTORM),
    THUNDERSTORM_MODERATE(96, WeatherCategory.THUNDERSTORM),
    THUNDERSTORM_HEAVY(99, WeatherCategory.THUNDERSTORM);

    private final int code;
    private final WeatherCategory category;

    public static WeatherCategory getCategoryForCode(int code) {
        return Arrays.stream(values())
                .filter(weatherCode -> weatherCode.getCode() == code)
                .map(WeatherCode::getCategory)
                .findFirst()
                .orElse(WeatherCategory.UNKNOWN);
    }
}

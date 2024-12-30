package com.codibly.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WeatherCategory {

    CLEAR_SKY("Clear sky"),
    MAINLY_CLEAR("Mainly clear"),
    PARTLY_CLOUDY("Partly cloudy"),
    OVERCAST("Overcast"),
    FOG("Fog"),
    DRIZZLE("Drizzle"),
    FREEZING_DRIZZLE("Freezing drizzle"),
    RAIN("Rain"),
    FREEZING_RAIN("Freezing rain"),
    SNOW("Snow"),
    SNOW_GRAINS("Snow grains"),
    RAIN_SHOWERS("Rain showers"),
    SNOW_SHOWERS("Snow showers"),
    THUNDERSTORM("Thunderstorm"),
    UNKNOWN("Unknown weather condition");

    private final String description;
}

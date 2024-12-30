package com.codibly.connectivity;

import com.codibly.dtos.WeatherDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WeatherAPIHandlerIntegrationTest {

    private static final double LATITUDE = 50.04968;
    private static final double LONGITUDE = 19.944544;
    private static final LocalDate START_DATE = LocalDate.now();
    private static final int EXPECTED_DAYS = 3;
    private static final int DAYS_TO_ADD = 2;

    @Autowired
    private WeatherAPIHandler weatherAPIHandler;

    @Test
    void fetchOneDayWeatherShouldReturnWeatherDTO() {
        //given/When
        Optional<WeatherDTO> result = weatherAPIHandler.fetchOneDayWeather(LATITUDE, LONGITUDE, START_DATE);

        //then
        assertTrue(result.isPresent());
        WeatherDTO weatherDTO = result.get();

        assertNotNull(weatherDTO.getMaxTemp());
        assertNotNull(weatherDTO.getMinTemp());
        assertTrue(weatherDTO.getMaxTemp() > weatherDTO.getMinTemp());
    }

    @Test
    void fetchWeatherForDateRangeShouldReturnWeatherDTOListForGivenTime() {
        //given
        final LocalDate endDate = START_DATE.plusDays(DAYS_TO_ADD);

        //when
        List<WeatherDTO> result = weatherAPIHandler.fetchWeatherForDateRange(LATITUDE, LONGITUDE, START_DATE, endDate);

        //then
        assertFalse(result.isEmpty());
        assertEquals(EXPECTED_DAYS, result.size());
    }
}

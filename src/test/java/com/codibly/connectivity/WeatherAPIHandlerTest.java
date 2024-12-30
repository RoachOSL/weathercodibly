package com.codibly.connectivity;

import com.codibly.dtos.WeatherDTO;
import com.codibly.mappers.WeatherJacksonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WeatherAPIHandlerTest {

    private static final double LATITUDE = 50.04968;
    private static final double LONGITUDE = 19.944544;
    private static final LocalDate START_DATE = LocalDate.now();
    private static final double MAX_TEMP_PARAMETER = 15.0;

    @Mock
    private WeatherJacksonMapper weatherJacksonMapper;

    @InjectMocks
    private WeatherAPIHandler weatherAPIHandler;

    private WeatherDTO mockWeatherDTO;

    @BeforeEach
    void setUp() {
        mockWeatherDTO = WeatherDTO.builder()
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .date(START_DATE)
                .maxTemp(MAX_TEMP_PARAMETER)
                .build();
    }

    @Test
    void fetchOneDayWeatherShouldReturnSuccessfulResponse() {
        //given
        given(weatherJacksonMapper.mapWeather(anyString())).willReturn(mockWeatherDTO);

        //when
        Optional<WeatherDTO> result = weatherAPIHandler.fetchOneDayWeather(LATITUDE, LONGITUDE, START_DATE);

        //then
        assertTrue(result.isPresent());
        assertEquals(mockWeatherDTO.getMaxTemp(), result.get().getMaxTemp());
        assertEquals(mockWeatherDTO, result.get());
    }

    @Test
    void fetchOneDayWeatherShouldReturnOptionalEmptyOnFailure() {
        //given
        given(weatherJacksonMapper.mapWeather(anyString())).willReturn(null);

        //when
        Optional<WeatherDTO> result = weatherAPIHandler.fetchOneDayWeather(LATITUDE, LONGITUDE, START_DATE);

        //then
        assertFalse(result.isPresent());
    }

    @Test
    void fetchWeatherForDateRangeShouldReturnDataForAllDays() {
        //given
        final int fullDaysRange = 3;
        final int daysToAdd = fullDaysRange - 1;

        LocalDate endDate = START_DATE.plusDays(daysToAdd);

        given(weatherJacksonMapper.mapWeather(anyString())).willReturn(mockWeatherDTO);

        //when
        List<WeatherDTO> result = weatherAPIHandler.fetchWeatherForDateRange(LATITUDE, LONGITUDE, START_DATE, endDate);

        //then
        assertEquals(fullDaysRange, result.size());
        for (WeatherDTO dto : result) {
            assertEquals(mockWeatherDTO.getMaxTemp(), dto.getMaxTemp());
            assertEquals(mockWeatherDTO, dto);
        }
    }

    @Test
    void fetchWeatherForDateRangeWithEmptyRangeShouldReturnEmptyList() {
        //given
        final int dayToSubtract = 1;
        LocalDate endDate = START_DATE.minusDays(dayToSubtract);

        //when
        List<WeatherDTO> result = weatherAPIHandler.fetchWeatherForDateRange(LATITUDE, LONGITUDE, START_DATE, endDate);

        //then
        assertTrue(result.isEmpty());
    }
}

package com.codibly.controller;

import com.codibly.dtos.WeatherForecastDTO;
import com.codibly.services.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WeatherController.class)
class WeatherControllerTest {

    private static final String WEATHER_FORECAST_ENDPOINT = "/weather/forecast";
    private static final String WEATHER_SUMMARY_ENDPOINT = "/weather/summary";
    private static final String PARAM_LATITUDE = "latitude";
    private static final String PARAM_LONGITUDE = "longitude";
    private static final String LATITUDE_OUT_OF_RANGE_ERROR = "Latitude must be >= -90";
    private static final String VALID_LATITUDE = "50.0";
    private static final String VALID_LONGITUDE = "19.0";
    private static final String NEGATIVE_LATITUDE = "-999.0";
    private static final String INVALID_LATITUDE = "invalid";
    private static final int BAD_REQUEST_CODE = 400;
    private static final String FIRST_MOCK_FORECAST_DATE = "2023-12-01";
    private static final String SECOND_MOCK_FORECAST_DATE = "2023-12-02";
    private static final double FIRST_MOCK_FORECAST_MAX_TEMP = 10.5;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherService weatherService;

    private List<WeatherForecastDTO> mockForecastList;

    @BeforeEach
    void setUp() {
        WeatherForecastDTO mockForecast1 = WeatherForecastDTO.builder()
                .date(LocalDate.parse(FIRST_MOCK_FORECAST_DATE))
                .maxTempC(FIRST_MOCK_FORECAST_MAX_TEMP)
                .build();

        WeatherForecastDTO mockForecast2 = WeatherForecastDTO.builder()
                .date(LocalDate.parse(SECOND_MOCK_FORECAST_DATE))
                .build();

        mockForecastList = List.of(mockForecast1, mockForecast2);
    }

    @Test
    void getWeeklyForecastShouldReturnForecastList() throws Exception {
        final double latitude = 50.0;
        final double longitude = 19.0;
        final int expectedForecastDays = 2;
        //given
        given(weatherService.getWeeklyForecast(latitude, longitude)).willReturn(mockForecastList);

        //when/then
        mockMvc.perform(get(WEATHER_FORECAST_ENDPOINT)
                        .param(PARAM_LATITUDE, VALID_LATITUDE)
                        .param(PARAM_LONGITUDE, VALID_LONGITUDE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedForecastDays))
                .andExpect(jsonPath("$[0].date").value(FIRST_MOCK_FORECAST_DATE))
                .andExpect(jsonPath("$[0].maxTempC").value(FIRST_MOCK_FORECAST_MAX_TEMP))
                .andExpect(jsonPath("$[1].date").value(SECOND_MOCK_FORECAST_DATE));
    }

    @Test
    void getWeeklySummaryShouldFailWithNegativeLatitude() throws Exception {
        //when/then
        mockMvc.perform(get(WEATHER_SUMMARY_ENDPOINT)
                        .param(PARAM_LATITUDE, NEGATIVE_LATITUDE)
                        .param(PARAM_LONGITUDE, VALID_LONGITUDE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST_CODE))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(LATITUDE_OUT_OF_RANGE_ERROR));
    }

    @Test
    void getWeeklyForecastShouldFailWithInvalidParameters() throws Exception {
        // when/then
        mockMvc.perform(get(WEATHER_FORECAST_ENDPOINT)
                        .param(PARAM_LATITUDE, INVALID_LATITUDE)
                        .param(PARAM_LONGITUDE, VALID_LONGITUDE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST_CODE));
    }
}

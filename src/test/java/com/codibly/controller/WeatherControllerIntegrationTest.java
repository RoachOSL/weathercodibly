package com.codibly.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WeatherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getWeeklyForecastShouldReturnForecastList() throws Exception {
        //given
        LocalDate today = LocalDate.now();

        //when/then
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/forecast")
                        .param("latitude", "50.0")
                        .param("longitude", "19.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(7))
                .andExpect(jsonPath("$[0].date").value(today.toString()))
                .andExpect(jsonPath("$[0].maxTempC").isNumber())
                .andExpect(jsonPath("$[0].minTempC").isNumber())
                .andExpect(jsonPath("$[0].estimatedEnergyKWh").isNumber());
    }

    @Test
    void getWeeklySummaryShouldReturnSummary() throws Exception {
        //when/then
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/summary")
                        .param("latitude", "50.0")
                        .param("longitude", "19.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avgPressureHPa").isNumber())
                .andExpect(jsonPath("$.avgSunshineDurationSeconds").isNumber())
                .andExpect(jsonPath("$.lowestTemperatureCelsius").isNumber())
                .andExpect(jsonPath("$.highestTemperatureCelsius").isNumber())
                .andExpect(jsonPath("$.forecastSummary").isString());
    }

    @Test
    void getWeeklyForecastShouldFailWithInvalidParameters() throws Exception {
        //when/then
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/forecast")
                        .param("latitude", "invalid")
                        .param("longitude", "19.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getWeeklySummaryShouldFailWithNegativeLatitude() throws Exception {
        //when/then
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/summary")
                        .param("latitude", "-999.0")
                        .param("longitude", "19.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

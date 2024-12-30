# Weather Service

Provides weather forecasts and estimates of solar energy production for the upcoming 7 days based on geographical
coordinates. It integrates with the Open-Meteo API to fetch weather data and includes calculations to estimate solar
energy production for photovoltaic systems.

## Features

- **Weather Forecast**  
  Provides a 7-day forecast including:
    - Date.
    - Weather code (e.g., clear, cloudy, rainy).
    - Minimum and maximum temperatures.
    - Estimated solar energy production (kWh).

- **Weather Summary**  
  Returns a weekly summary with:
    - Average atmospheric pressure for the week.
    - Average daily sun exposure time.
    - Extreme temperatures (highest and lowest) during the week.
    - General weekly weather summary.

## Running Instructions

The service uses Docker for containerization. By default:

- Docker runs on port `8080`.

### How to Run It?

1. **Running the Docker Container:**
    - Build the Docker Image:
      ```bash
      docker build -t weather-app .
      ```
    - Run the Docker Container:
      ```bash
      docker run -p 8080:8080 weather-app
      ```
    - Stopping the Service:
      ```bash
      docker stop <container_id>
      ```
      or u can also just use "Ctrl" + "C" shortcut.

---

## Endpoints

### 1. **Weather Forecast**

**GET** `/weather/forecast`  
Fetches the 7-day weather forecast along with solar energy production estimates.

**Request Parameters:**

- `latitude` (required, float): Latitude of the location. Must be between -90 and 90.
- `longitude` (required, float): Longitude of the location. Must be between -180 and 180.

**Example request:**

```
http://localhost:8080/weather/forecast?latitude=50.049683&longitude=19.944544
```

**Example response for two days:**

```json
[
  {
    "date": "2024-12-30",
    "weatherCodes": [
      3
    ],
    "maxTempC": 4,
    "minTempC": -1,
    "estimatedEnergyKWh": 3.34
  },
  {
    "date": "2024-12-31",
    "weatherCodes": [
      3
    ],
    "maxTempC": 6,
    "minTempC": 0,
    "estimatedEnergyKWh": 3.34
  }
]
```

**Response description:**

- date: The date for the forecasted day.
- weatherCodes: Numeric codes representing weather conditions.
- maxTempC: Maximum temperature in Celsius.
- minTempC: Minimum temperature in Celsius.
- estimatedEnergyKWh: Estimated solar energy production in kWh.

### 2. **Weekly Weather Summary**

**GET** `/weather/summary`  
Provides a summary of the weather for the week, including average pressure, average sunshine duration, extreme
temperatures, and a general description of the week's weather.

**Request Parameters:**

- `latitude` (required, float): Latitude of the location. Must be between -90 and 90.
- `longitude` (required, float): Longitude of the location. Must be between -180 and 180.

**Example request:**

```
http://localhost:8080/weather/summary?latitude=50.049683&longitude=19.944544
```

**Example response:**

  ```json
  {
  "avgPressureHPa": 1020.31,
  "avgSunshineDurationSeconds": 19577.5,
  "lowestTemperatureCelsius": -5.5,
  "highestTemperatureCelsius": 9.6,
  "forecastSummary": "Overcast"
}
  ```

**Response description:**

- avgPressureHPa: The average atmospheric pressure for the week in hPa.
- avgSunshineDurationSeconds: The average daily sunshine duration for the week in seconds.
- lowestTemperatureCelsius: The lowest temperature during the week in Celsius.
- highestTemperatureCelsius: The highest temperature during the week in Celsius.

---

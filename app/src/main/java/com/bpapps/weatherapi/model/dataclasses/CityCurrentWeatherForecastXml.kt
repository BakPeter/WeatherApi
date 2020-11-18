package com.bpapps.weatherapi.model.dataclasses

data class CityCurrentWeatherForecastXml(
    val city: City,
    val temperature: Temperature,
    val weather: WeatherXml,
    val cod: Int = -1,
    val message: String = ""
) : CityCurrentWeatherForecast()

data class City(val name: String)
data class Temperature(val value: Double)
data class WeatherXml(val value: String)
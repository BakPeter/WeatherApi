package com.bpapps.weatherapi.model.dataclasses

data class CityCurrentWeatherForecastJson(
    val name: String,
    val coord: Coord,
    val weather: ArrayList<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val cod: Int,
    val sys: Sys,
    val message: String
) : CityCurrentWeatherForecast()

data class Wind(val speed: Double, val deg: Double)
data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Double,
    val humidity: Double
)

data class Weather(val id: Int, val main: String, val description: String, val icon: String)
data class Coord(val lon: Double, val lat: Double)
data class Sys(val message: String)

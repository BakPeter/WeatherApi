package com.bpapps.weatherapi.model.dataclasses

data class Response(
    val rowData: String?,
    val error: Exception?,
    val requestParameters: RequestParameters,
    val result: CityCurrentWeatherForecast?
) {
}

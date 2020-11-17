package com.bpapps.weatherapi

import android.app.DownloadManager

data class Response(
    val data: String?,
    val error: Exception?,
    val requestParameters: RequestParameters,
    val result: CityCurrentWeatherForecast?
) {
}

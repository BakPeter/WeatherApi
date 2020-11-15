package com.bpapps.weatherapi

class WeatherApiRepository private constructor() {


    companion object {
        private var instance: WeatherApiRepository? = null

        fun initialize(): WeatherApiRepository {
            if (instance == null) {
                instance = WeatherApiRepository()
            }

            return instance!!
        }

        fun getInstance(): WeatherApiRepository {
            if (instance == null)
                throw IllegalStateException("WeatherApi repository must be initialized")

            return instance!!
        }
    }
}
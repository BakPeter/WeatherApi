package com.bpapps.weatherapi

class Model private constructor() {

    private val repository = WeatherApiRepository.initialize()

    companion object {
        var instance: Model? = null
            private set
            get() {
                if (instance == null) {
                    instance = Model()
                }

                return instance
            }
    }
}
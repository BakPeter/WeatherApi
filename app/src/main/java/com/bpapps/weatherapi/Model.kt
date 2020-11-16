package com.bpapps.weatherapi

class Model private constructor() {

    private val repository = WeatherApiRepository.initialize()

    companion object {
        private var instance: Model? = null

        fun getInstance(): Model {
            if (instance == null) {
                instance = Model()
            }

            return instance!!
        }
    }
}
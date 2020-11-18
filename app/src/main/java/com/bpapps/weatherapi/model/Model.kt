package com.bpapps.weatherapi.model

import com.bpapps.weatherapi.model.dataclasses.RequestParameters
import com.bpapps.weatherapi.model.dataclasses.Response

class Model private constructor() {
    fun processWebService(
        parameters: RequestParameters,
        callBack: IWebServiceRequest?
    ) {
        repository.processWebService(
            parameters,
            object :
                WeatherApiRepository.IWebServiceRequest {
                override fun onResponseReceived(response: Response) {
                    callBack?.onResponseReceived(response)
                }
            })
    }

    private val repository =
        WeatherApiRepository.initialize()

    companion object {
        const val CITY_NOT_FOUND: Int =
            WeatherApiRepository.CITY_NOT_FOUND
        private var instance: Model? = null

        fun getInstance(): Model {
            if (instance == null) {
                instance =
                    Model()
            }

            return instance!!
        }
    }

    interface IWebServiceRequest {
        fun onResponseReceived(response: Response)
    }
}
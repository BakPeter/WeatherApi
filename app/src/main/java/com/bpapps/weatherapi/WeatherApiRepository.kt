package com.bpapps.weatherapi

class WeatherApiRepository private constructor() {
    private val webServiceUtility: WebServicesApiUtility = WebServicesApiUtility()

    companion object {
        const val CITY_NOT_FOUND: Int = WebServicesApiUtility.CITY_NOT_FOUND

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

    fun processWebService(
        parameters: RequestParameters,
        callBack: IWebServiceRequest?
    ) {
        webServiceUtility.processWebService(
            parameters,
            object : WebServicesApiUtility.IWebServiceRequest {
                override fun onResponseReceived(response: Response) {
                    callBack?.onResponseReceived(response)
                }
            })
    }

    interface IWebServiceRequest {
        fun onResponseReceived(response: Response)
    }
}
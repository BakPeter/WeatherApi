package com.bpapps.weatherapi.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.bpapps.weatherapi.*
import com.bpapps.weatherapi.model.Model
import com.bpapps.weatherapi.model.dataclasses.CityCurrentWeatherForecastJson
import com.bpapps.weatherapi.model.dataclasses.CityCurrentWeatherForecastXml
import com.bpapps.weatherapi.model.dataclasses.RequestParameters
import com.bpapps.weatherapi.model.dataclasses.Response
import com.bpapps.weatherapi.model.web.WebServicesApiUtility

@SuppressLint("LongLogTag")
class MainViewModel : ViewModel() {

    private val model = Model.getInstance()
    private var webServiceRequestListener: IWebServiceRequest? = null

    var tvResultShowerText: String = ""
        private set

    var tvRowDataShowerText: String = ""
        private set


    val architectureTypes: ArrayList<String> =
        arrayListOf(
            WebServicesApiUtility.ARCHITECTURE_TYPE_HTTP,
            WebServicesApiUtility.ARCHITECTURE_TYPE_VOLLEY
        )
    val dataTypes: ArrayList<String> =
        arrayListOf(
            WebServicesApiUtility.DATA_TYPE_JSON,
            WebServicesApiUtility.DATA_TYPE_XML
        )

    var cityName: String = ""
    var architectureType: String = architectureTypes[0]
    var dataType: String = dataTypes[0]

    fun getWeather() {
//        Log.d(TAG, "Processing web service")
        model.processWebService(
            RequestParameters(
                cityName,
                architectureType,
                dataType
            ),
            object : Model.IWebServiceRequest {
                override fun onResponseReceived(response: Response) {
                    tvRowDataShowerText = "ROW DATA : '\n'${response.rowData}"

                    if (response.requestParameters.dataType == WebServicesApiUtility.DATA_TYPE_JSON) {
                        val result = response.result as CityCurrentWeatherForecastJson
                        tvResultShowerText = if (result.cod == Model.CITY_NOT_FOUND) {
                            result.message
                        } else {
                            "${result.name} : ${result.weather[0].description}, ${result.main.temp}${
                                App.getInstance()?.resources?.getString(
                                    (R.string.degree)
                                )
                            }C"
                        }
                    } else {
                        val result = response.result as CityCurrentWeatherForecastXml
                        tvResultShowerText = if (result.cod == Model.CITY_NOT_FOUND) {
                            result.message
                        } else {
                            "${result.city.name} : ${result.weather.value}, ${result.temperature.value}${
                                App.getInstance()?.resources?.getString(
                                    (R.string.degree)
                                )
                            }C"
                        }
                    }


                    webServiceRequestListener?.onResponseReceived(response)
                }
            })
    }

    companion object {
        private const val TAG = "TAG.com.bpapps.weatherapi.viewmodel.MainViewModel"
    }

    fun registerForWbServiceRequest(callBack: IWebServiceRequest) {
        webServiceRequestListener = callBack
    }

    fun unRegisterForWbServiceRequest() {
        webServiceRequestListener = null
    }

    interface IWebServiceRequest {
        fun onResponseReceived(response: Response)
    }
}
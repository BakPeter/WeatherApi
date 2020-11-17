package com.bpapps.weatherapi

import android.annotation.SuppressLint
import android.util.Log
import android.view.Display
import androidx.lifecycle.ViewModel
import java.lang.Exception

@SuppressLint("LongLogTag")
class MainViewModel : ViewModel() {

    private val model = Model.getInstance()

    private var webServiceRequestListener: IWebServiceRequest? = null

    val architectureTypes: ArrayList<String> =
        arrayListOf(
            WebServicesApiUtility.ARCHITECTURE_TYPE_HTTP,
            WebServicesApiUtility.ARCHITECTURE_TYPE_VOLLEY
        )
    val dataTypes: ArrayList<String> =
        arrayListOf(WebServicesApiUtility.DATA_TYPE_JSON, WebServicesApiUtility.DATA_TYPE_XML)

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
                    webServiceRequestListener?.onResponseReceived(response)
                }
            })
    }

    companion object {
        private const val TAG = "TAG.com.bpapps.weatherapi.MainViewModel"
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
package com.bpapps.weatherapi

import android.annotation.SuppressLint
import android.util.Log
import android.view.Display
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.main_fragment.*
import java.lang.Exception

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
                    tvRowDataShowerText = "ROW DATA : '\n'${response.data}"
                    tvResultShowerText = if (response.result?.cod == Model.CITY_NOT_FOUND) {
//                       "City not found"
                        response.result.message
                    } else {
                        "${response.result?.name} : ${response.result?.weather?.get(0)?.description}, ${response.result?.main?.temp}${App.getInstance()?.resources?.getString((R.string.degree))}C"
                    }

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
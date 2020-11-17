package com.bpapps.weatherapi

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder

@SuppressLint("LongLogTag")
class WebServicesApiUtility {
    fun processWebService(
        parameters: RequestParameters,
        callBack: IWebServiceRequest?
    ) {
        val apiUrlRequest = getApiUrl(parameters)
//        Log.d(TAG, apiUrlRequest)

        when {
            parameters.cityName == "" -> {
                callBack?.onResponseReceived(
                    Response(
                        null,
                        Exception("Enter valid city name"),
                        parameters,
                        null
                    )
                )
            }
            parameters.architectureType == "ARCHITECTURE_TYPE_VOLLEY" -> {
                callBack?.onResponseReceived(
                    Response(
                        null,
                        Exception("ARCHITECTURE_TYPE_VOLLEY is not implemented"),
                        parameters,
                        null
                    )
                )
            }

            parameters.dataType == "DATA_TYPE_XML" -> {
                callBack?.onResponseReceived(
                    Response(
                        null,
                        Exception("DATA_TYPE_XML is not implemented"),
                        parameters,
                        null
                    )
                )
            }

            parameters.architectureType == ARCHITECTURE_TYPE_HTTP -> {
                val request = Request.Builder().url(apiUrlRequest).build()

                val client = OkHttpClient()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        HandlerCompat.createAsync(Looper.getMainLooper()).also { handler ->
                            handler.post {
                                callBack?.onResponseReceived(
                                    Response(
                                        null,
                                        null,
                                        parameters,
                                        null
                                    )
                                )
                            }
                        }
                    }

                    override fun onResponse(call: Call, response: okhttp3.Response) {
                        val body = response.body?.string()
                        Log.d(TAG, body!!)

                        var weatherForecast: CityCurrentWeatherForecast? = null

                        when (parameters.dataType) {
                            DATA_TYPE_JSON -> {
                                val gson = GsonBuilder().create()
                                weatherForecast =
                                    gson.fromJson(body, CityCurrentWeatherForecast::class.java)
                            }
                            else -> {
                                //DATA_TYPE_XML ->

                            }
                        }

                        Log.d(TAG, weatherForecast.toString())
                        HandlerCompat.createAsync(Looper.getMainLooper()).also { handler ->
                            handler.post {
                                callBack?.onResponseReceived(
                                    Response(
                                        body,
                                        null,
                                        parameters,
                                        weatherForecast
                                    )
                                )
                            }
                        }
                    }
                })
            }
        }
    }

    private fun getApiUrl(parameters: RequestParameters): String {
        val urlBuilder = StringBuilder(URL)
        urlBuilder.append("${Q}=${parameters.cityName}")
        urlBuilder.append("&${APP_ID}=${API_KEY}")

        if (parameters.dataType == DATA_TYPE_JSON) {
            urlBuilder.append("&${MODE}=${MODE_JSON}")
        } else if (parameters.dataType == DATA_TYPE_XML) {
            urlBuilder.append("&${MODE}=${MODE_XML}")
        }

        urlBuilder.append("&${UNITS}=${UNITS_METRIC}")

        return urlBuilder.toString()
    }

    companion object {
        private const val TAG = "TAG.com.bpapps.weatherapi.WebServicesApiUtility"

        private const val API_KEY = "e97176925ceb0c56da4abf8c7905f255"
        private const val URL = "https://api.openweathermap.org/data/2.5/weather?"
        private const val Q = "q"
        private const val APP_ID = "appid"
        private const val MODE = "mode"
        private const val UNITS = "units"
        private const val UNITS_METRIC = "metric"
        private const val MODE_XML = "xml"
        private const val MODE_JSON = "json"

        const val DATA_TYPE_JSON = "DATA_TYPE_JSON"
        const val DATA_TYPE_XML = "DATA_TYPE_XML"

        const val ARCHITECTURE_TYPE_HTTP = "ARCHITECTURE_TYPE_HTTP"
        const val ARCHITECTURE_TYPE_VOLLEY = "ARCHITECTURE_TYPE_VOLLEY"

        const val CITY_NOT_FOUND = 404
    }

    interface IWebServiceRequest {
        fun onResponseReceived(response: Response)
    }
}
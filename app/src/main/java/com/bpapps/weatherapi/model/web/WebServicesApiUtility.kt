package com.bpapps.weatherapi.model.web

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import com.android.volley.toolbox.Volley
import com.bpapps.weatherapi.App
import com.bpapps.weatherapi.model.dataclasses.CityCurrentWeatherForecast
import com.bpapps.weatherapi.model.dataclasses.RequestParameters
import com.bpapps.weatherapi.model.dataclasses.Response
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

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

                        weatherForecast = when (parameters.dataType) {
                            DATA_TYPE_JSON -> {
                                val gson = GsonBuilder().create()
                                gson.fromJson(body, CityCurrentWeatherForecast::class.java)
                            }
                            else -> {
                                val x = 1
                                //DATA_TYPE_XML ->
                                WeatherApiXmlParser().parse(response.body!!.byteStream())
                            }
                        }

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

            parameters.architectureType == ARCHITECTURE_TYPE_VOLLEY -> {
                when (parameters.dataType) {
                    DATA_TYPE_JSON -> {
                        val weatherForecastRequest =
                            WeatherApiGsonRequest(
                                apiUrlRequest,
                                CityCurrentWeatherForecast::class.java,
                                null,
                                com.android.volley.Response.Listener { response ->
                                    callBack?.onResponseReceived(
                                        Response(
                                            "Row data is unavailable for Volley",
                                            null,
                                            parameters,
                                            response
                                        )
                                    )
                                },
                                com.android.volley.Response.ErrorListener { error ->
                                    if (error?.networkResponse?.statusCode == 404) {
                                        val gson = GsonBuilder().create()
                                        val json = String(
                                            error.networkResponse.data.toTypedArray().toByteArray()
                                        )
                                        val weatherForecast =
                                            gson.fromJson(
                                                json, CityCurrentWeatherForecast::class.java
                                            )

                                        callBack?.onResponseReceived(
                                            Response(
                                                json,
                                                null,
                                                parameters,
                                                weatherForecast
                                            )
                                        )
                                    } else {
                                        callBack?.onResponseReceived(
                                            Response(
                                                null,
                                                Exception(error.message),
                                                parameters,
                                                null
                                            )
                                        )
                                    }
                                })

                        val queue = Volley.newRequestQueue(App.getInstance())
                        queue.add(weatherForecastRequest)

//                        val jsonObjectRequest =
//                            JsonObjectRequest(
//                                com.android.volley.Request.Method.GET,
//                                apiUrlRequest,
//                                null,
//                                com.android.volley.Response.Listener { response ->
//                                    val x = 1
//                                },
//                                com.android.volley.Response.ErrorListener { error ->
//                                    val x = 1
//                                }
//                            )
//                        val queue = Volley.newRequestQueue(App.getInstance())
//
//                        queue.add(jsonObjectRequest)
                    }

                    DATA_TYPE_XML -> {
                        callBack?.onResponseReceived(
                            Response(
                                null,
                                Exception("DATA_TYPE_XML is not implemented"),
                                parameters,
                                null
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getApiUrl(parameters: RequestParameters): String {
        val urlBuilder = StringBuilder(URL)
        urlBuilder.append("$Q=${parameters.cityName}")
        urlBuilder.append("&$APP_ID=$API_KEY")

        if (parameters.dataType == DATA_TYPE_JSON) {
            urlBuilder.append("&$MODE=$MODE_JSON")
        } else if (parameters.dataType == DATA_TYPE_XML) {
            urlBuilder.append("&$MODE=$MODE_XML")
        }

        urlBuilder.append("&$UNITS=$UNITS_METRIC")

        return urlBuilder.toString()
    }

    companion object {
        private const val TAG = "TAG.com.bpapps.weatherapi.model.web.WebServicesApiUtility"

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
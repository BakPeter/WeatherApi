package com.bpapps.weatherapi.model.web

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import java.io.InputStream

class WeatherApiXMLRequest<T>(
    url: String,
    private val clazz: Class<T>,
    private val headers: MutableMap<String, String>?,
    private val listener: com.android.volley.Response.Listener<T>,
    errorListener: com.android.volley.Response.ErrorListener
) : Request<T>(Method.GET, url, errorListener) {

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val inputStream = response?.data?.inputStream()
            val parser = WeatherApiXmlParser()
            Response.success(
                parser.parse(inputStream!!) as T,
                HttpHeaderParser.parseCacheHeaders(response)
            )
        } catch (e: Exception) {
            Response.error(ParseError(e))
        }

    }

    override fun getHeaders(): MutableMap<String, String> = headers ?: super.getHeaders()
    override fun deliverResponse(response: T) = listener.onResponse(response)
}
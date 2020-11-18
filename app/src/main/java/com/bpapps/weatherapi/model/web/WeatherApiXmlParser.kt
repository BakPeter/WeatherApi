package com.bpapps.weatherapi.model.web

import android.util.Xml
import com.bpapps.weatherapi.model.dataclasses.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

private val ns: String? = null

class WeatherApiXmlParser {
    fun parse(inputStream: ByteArrayInputStream): CityCurrentWeatherForecast {
        inputStream.use {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    private fun readFeed(parser: XmlPullParser): CityCurrentWeatherForecastXml {
        var cityName: String = ""
        var temperature: String = "-1"
        var weatherDescription: String = ""
        var cod = "-1"
        var message = ""

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    WebServicesApiUtility.CITY_XML_TAG -> {
                        cityName = parser.getAttributeValue(
                            null,
                            WebServicesApiUtility.CITY_XML_ATTRIBUTE_NAME
                        )
                    }

                    WebServicesApiUtility.TEMPERATURE_XML_TAG -> {
                        temperature = parser.getAttributeValue(
                            null,
                            WebServicesApiUtility.TEMPERATURE_XML_ATTRIBUTE_VALUE
                        )

                    }

                    WebServicesApiUtility.WEATHER_XML_TAG -> {
                        weatherDescription = parser.getAttributeValue(
                            null,
                            WebServicesApiUtility.WEATHER_XML_ATTRIBUTE_VALUE
                        )
                    }

                    WebServicesApiUtility.COD_XML_TAG -> {
                        cod = parser.nextText()
                    }

                    WebServicesApiUtility.MESSAGE_XML_ATTRIBUTE_VALUE -> {
                        message = parser.nextText()
                    }
                }
            }

            eventType = parser.next()
        }

        return CityCurrentWeatherForecastXml(
            City(cityName),
            Temperature(temperature.toDouble()),
            WeatherXml(weatherDescription),
            cod.toInt(),
            message
        )
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType == XmlPullParser.START_TAG) {
            throw IllegalStateException("The parser is in START_TAG")
        }

        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.START_TAG -> depth++
                XmlPullParser.END_TAG -> depth--
            }
        }
    }
}
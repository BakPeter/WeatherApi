package com.bpapps.weatherapi.model.web

import android.util.Xml
import com.bpapps.weatherapi.model.dataclasses.CityCurrentWeatherForecast
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

private val ns: String? = null

class WeatherApiXmlParser {
    //city name, weather description, d

    fun parse(inputStream: InputStream): CityCurrentWeatherForecast {
        inputStream.use {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    private fun readFeed(parser: XmlPullParser): CityCurrentWeatherForecast {
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
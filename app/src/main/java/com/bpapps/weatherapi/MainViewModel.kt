package com.bpapps.weatherapi

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val model = Model.instance

    var cityName: String? = null
    var architectureType: String = "ARCHITECTURE_TYPE_HTTP"
    var dataType: String = "DATA_TYPE_JSON"

    val architectureTypes: ArrayList<String> =
        arrayListOf("ARCHITECTURE_TYPE_HTTP", "ARCHITECTURE_TYPE_VOLLEY")

    val dataTypes: ArrayList<String> = arrayListOf("DATA_TYPE_JSON", "DATA_TYPE_XML")

    companion object {
        const val DATA_TYPE_JSON = 1
        const val DATA_TYPE_XML = 2

        const val ARCHITECTURE_TYPE_HTTP = 3
        const val ARCHITECTURE_TYPE_VOLLEY = 4
    }
}
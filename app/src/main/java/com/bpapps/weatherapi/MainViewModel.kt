package com.bpapps.weatherapi

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var cityName: String? = null

    var dataType: Int = NOUN
        set(value) {
            field = when(value) {
                JSON, XML, VOLLEY -> {
                    value
                }

                else ->{
                    NOUN
                }
            }
        }

    companion object {
        const val NOUN = 0
        const val JSON = 1
        const val XML = 2

        const val
        const val VOLLEY = 3
    }
}
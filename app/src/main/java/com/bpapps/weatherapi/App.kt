package com.bpapps.weatherapi

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    companion object {
        private var instance: App? = null

        fun getInstance() = instance
    }
}
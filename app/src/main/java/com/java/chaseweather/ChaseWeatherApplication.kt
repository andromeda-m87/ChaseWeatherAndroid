package com.java.chaseweather

import android.app.Application
import java.lang.ref.WeakReference

class ChaseWeatherApplication: Application() {
    companion object {
        lateinit var application: WeakReference<ChaseWeatherApplication>
    }

    override fun onCreate() {
        super.onCreate()
        application = WeakReference(this)
    }
}
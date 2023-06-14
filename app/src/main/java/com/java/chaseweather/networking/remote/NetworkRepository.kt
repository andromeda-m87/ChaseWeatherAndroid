package com.java.chaseweather.networking.remote

import android.location.Location
import com.java.chaseweather.model.CurrentWeatherResponse

interface NetworkRepository {
    suspend fun fetchCurrentWeather(
        location: Location,
        callback: (Result<CurrentWeatherResponse>) -> Unit
    )
}
package com.java.chaseweather.model

import com.google.gson.annotations.SerializedName

class CurrentWeatherResponse {
    @SerializedName("weather")
    var weather: List<WeatherResponse>? = null

    @SerializedName("main")
    var main: MainResponse? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("dt")
    var date: Long? = null

    @SerializedName("wind")
    var wind: WindResponse? = null
}
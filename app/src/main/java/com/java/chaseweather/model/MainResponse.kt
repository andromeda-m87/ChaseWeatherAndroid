package com.java.chaseweather.model
import com.google.gson.annotations.SerializedName
class MainResponse {
    @SerializedName("feels_like")
    var feelsLike: Double? = null

    @SerializedName("temp")
    var temp: Double? = null

    @SerializedName("temp_max")
    var tempMax: Double? = null

    @SerializedName("temp_min")
    var tempMin: Double? = null

    @SerializedName("humidity")
    var humidity: Int? = null
}

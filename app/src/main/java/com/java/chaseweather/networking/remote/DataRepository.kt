package com.java.chaseweather.networking.remote

import android.location.Location
import com.java.chaseweather.model.CurrentWeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataRepository(
    private val weatherApi: ApiService
) : NetworkRepository {
    override suspend fun fetchCurrentWeather(
        location: Location,
        callback: (Result<CurrentWeatherResponse>) -> Unit
    ) {
        weatherApi.getCurrentWeather(
            location.latitude,
            location.longitude,
            "65ba7ba035eaa08d075c26cc3518f746"
        ).enqueue(object : Callback<CurrentWeatherResponse> {
            override fun onResponse(
                call: Call<CurrentWeatherResponse>,
                response: Response<CurrentWeatherResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.invoke(Result.success(response.body()!!))
                } else {
                    callback.invoke(Result.failure(Throwable(response.code().toString())))
                }
            }

            override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                callback.invoke(Result.failure(Throwable(t)))
            }
        })
    }
}

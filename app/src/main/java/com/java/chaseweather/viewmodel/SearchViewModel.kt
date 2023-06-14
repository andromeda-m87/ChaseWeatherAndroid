package com.java.chaseweather.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.java.chaseweather.location.LocationRepository
import com.java.chaseweather.networking.remote.NetworkRepository
import com.java.chaseweather.model.CurrentWeatherResponse
import com.java.chaseweather.utils.SharedPref
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val locationRepo: LocationRepository,
    private val networkRepo: NetworkRepository
): ViewModel() {

    private val _cityNameLiveData = MutableLiveData<String>()
    val cityNameLiveData: LiveData<String> = _cityNameLiveData

    private val _locationLiveData = MutableLiveData<Location>()
    val locationLiveData: LiveData<Location> = _locationLiveData

    private val _currentWeatherLiveData = MutableLiveData<CurrentWeatherResponse>()
    val currentWeatherLiveData: LiveData<CurrentWeatherResponse> = _currentWeatherLiveData

    private val _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = _errorLiveData

    private val _progressBarLiveData = MutableLiveData<Boolean>()
    val progressBarLiveData: LiveData<Boolean> = _progressBarLiveData

    init {
        // Observe changes to _locationLiveData and trigger getCurrentWeather()
        locationLiveData.observeForever { location ->
            location?.let { getCurrentWeather(it) }
        }
    }

    fun getLocation() {
        viewModelScope.launch {
            val cityName = SharedPref.getCityName()
            if (cityName.isEmpty())
                locationRepo.currentLocation() {
                    _locationLiveData.value = it
                } else {
                getLocationFromAddressName(cityName)
            }
        }
    }

    fun getCurrentWeather(location: Location) {
        viewModelScope.launch {
            networkRepo.fetchCurrentWeather(location) {
                if (it.isSuccess) {
                    _currentWeatherLiveData.value = it.getOrNull()
                } else {
                    _errorLiveData.value = it.exceptionOrNull()
                }
            }
        }
        _progressBarLiveData.value = false
    }

    fun getLocationFromAddressName(name: String) {
        if (name.isNotBlank()) {
            SharedPref.saveCityName(name)
            _progressBarLiveData.value = true
            viewModelScope.launch {
                val address = locationRepo.getLocationFromAddressName(name)
                if (address != null) {
                    val location = Location("")
                    location.latitude = address.latitude
                    location.longitude = address.longitude
                    _locationLiveData.value = location
                } else {
                    _errorLiveData.value = Throwable("can't find this address")
                }
            }
        }
    }

    fun removeLocationUpdates() {
        viewModelScope.launch {
            locationRepo.removeUpdates()
        }
    }
}
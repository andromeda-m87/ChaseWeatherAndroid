package com.java.chaseweather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.java.chaseweather.location.LocationRepository
import com.java.chaseweather.networking.remote.NetworkRepository

class SearchViewModelFactory(
    private val locationRepository: LocationRepository,
    private val networkRepository: NetworkRepository
):
    ViewModelProvider.AndroidViewModelFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        SearchViewModel(locationRepository, networkRepository) as T
    }

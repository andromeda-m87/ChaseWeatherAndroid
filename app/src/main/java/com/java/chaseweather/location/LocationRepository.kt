package com.java.chaseweather.location

import android.location.Address
import android.location.Location

interface LocationRepository {
    suspend fun currentLocation(callback: (Location) -> Unit)
    suspend fun getLocationFromAddressName(name: String): Address?
    suspend fun removeUpdates()
}

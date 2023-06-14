package com.java.chaseweather.location.impl

import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import com.java.chaseweather.location.LocationRepository
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

class LocationRepositoryImpl(context: Context) : LocationRepository {

    private val locationService = context.getSystemService(Context.LOCATION_SERVICE)
    private val locationManager: LocationManager = locationService as LocationManager
    private lateinit var locationListener: LocationListener
    private val geocoder = Geocoder(context)
    private var isLocationListenerStarted = false

    @SuppressLint("MissingPermission")
    override suspend fun currentLocation(callback: (Location) -> Unit) {
        if (!isLocationListenerStarted) {
            isLocationListenerStarted = true
            locationListener = LocationListener { location ->
                callback.invoke(location)
            }

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                REFRESH_RATE,
                TRASH_HOLD,
                locationListener
            )

            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                REFRESH_RATE,
                TRASH_HOLD,
                locationListener
            )
        }
    }

    override suspend fun getLocationFromAddressName(name: String): Address? =
        suspendCoroutine {
            thread {
                val result = geocoder.getFromLocationName(name, 1)

                if (result != null) {
                    it.resumeWith(Result.success(result.firstOrNull()))
                } else {
                    it.resumeWith(Result.failure(Throwable("can't find this address")))
                }
            }
        }

    override suspend fun removeUpdates() {
        if (isLocationListenerStarted) {
            locationManager.removeUpdates(locationListener)
            isLocationListenerStarted = false
        }
    }

    companion object {
        private const val REFRESH_RATE = 60_000L
        private const val TRASH_HOLD = 100f
    }
}

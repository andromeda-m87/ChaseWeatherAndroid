package com.java.chaseweather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.java.chaseweather.di.AppComponent
import com.java.chaseweather.di.AppModule
import com.java.chaseweather.di.DaggerAppComponent
import com.java.chaseweather.ui.theme.ChaseWeatherTheme
import com.java.chaseweather.ui.theme.PrimaryDark
import com.java.chaseweather.viewmodel.SearchViewModel
import javax.inject.Inject


class MainActivity : ComponentActivity() {

    @Inject
    lateinit var searchViewModel: SearchViewModel

    private lateinit var appComponent: AppComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(application))
            .build()

        appComponent.inject(this)

        if (savedInstanceState == null) {
            if (canAccessLocation()) searchViewModel.getLocation()
            else checkLocationPermission()
        }

        setContent {
            ChaseWeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = PrimaryDark
                ) {
                    WeatherViewLayout(searchViewModel)
                }
            }
        }
    }

    private fun checkLocationPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            (
                    ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    ) -> {
                searchViewModel.getLocation()
            }
            else -> {
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                searchViewModel.getLocation()
            }
        }

    private fun canAccessLocation(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun hasPermission(perm: String): Boolean {
        return PermissionChecker.PERMISSION_GRANTED == PermissionChecker.checkSelfPermission(
            applicationContext,
            perm
        )
    }

    override fun onStop() {
        super.onStop()
        searchViewModel.removeLocationUpdates()
    }
}
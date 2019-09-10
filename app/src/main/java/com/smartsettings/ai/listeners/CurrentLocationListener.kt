package com.smartsettings.ai.listeners

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.smartsettings.ai.SmartApp
import javax.inject.Inject

class CurrentLocationListener : LiveData<Location>() {

    @Inject
    lateinit var context: Context

    init {
        SmartApp.appComponent.inject(this)
    }

    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
            if (p0 != null) {
                value = p0.lastLocation
            }
        }
    }

    @RequiresPermission(value = Manifest.permission.ACCESS_FINE_LOCATION)
    fun startLocationUpdates() {

        val locationRequest = LocationRequest()
        locationRequest.fastestInterval = 10000 / 2
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        FusedLocationProviderClient(context).requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        FusedLocationProviderClient(context).removeLocationUpdates(locationCallback)
    }
}
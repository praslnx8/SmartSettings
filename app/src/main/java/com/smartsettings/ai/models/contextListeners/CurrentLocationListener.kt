package com.smartsettings.ai.models.contextListeners

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.models.contextData.LocationContext
import javax.inject.Inject

class CurrentLocationListener : ContextListener<LocationContext>() {

    @Inject
    lateinit var context: Context

    private var contextChangeCallback: ((LocationContext) -> Unit)? = null

    init {
        SmartApp.appComponent.inject(this)
    }

    @SuppressLint("MissingPermission")
    override fun startListeningToContextChanges(contextChangeCallback: (LocationContext) -> Unit) {
        this.contextChangeCallback = contextChangeCallback
        startLocationUpdates()
    }

    override fun stopListeningToContextChanges() {
        stopLocationUpdates()
    }

    override fun askListeningPermissionIfAny(permissionGrantCallback: (Boolean) -> Unit) {

    }

    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
            if (p0 != null && p0.lastLocation != null) {
                contextChangeCallback?.let { it(LocationContext(p0.lastLocation.latitude, p0.lastLocation.longitude)) }
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
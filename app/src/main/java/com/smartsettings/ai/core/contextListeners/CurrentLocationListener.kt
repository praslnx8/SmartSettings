package com.smartsettings.ai.core.contextListeners

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.data.contextData.LocationContext
import com.smartsettings.permissionhelper.PermissionManager
import javax.inject.Inject

class CurrentLocationListener : ContextListener() {

    override fun getContextData(): LocationContext? {
        return locationContext
    }

    @Inject
    lateinit var context: Context

    private var contextChangeCallback: (() -> Unit)? = null

    private var locationContext: LocationContext? = null

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    init {
        SmartApp.appComponent.inject(this)
    }

    @SuppressLint("MissingPermission")
    override fun startListeningToContextChanges(contextChangeCallback: () -> Unit) {
        this.contextChangeCallback = contextChangeCallback
        startLocationUpdates()
    }

    override fun isListeningPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun askListeningPermission(permissionGrantCallback: (Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            PermissionManager.requestPermission(
                context,
                21,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            ) {
                permissionGrantCallback(it.isAllGranted())
            }
        } else {
            PermissionManager.requestPermission(context, 21, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)) {
                permissionGrantCallback(it.isAllGranted())
            }
        }
    }

    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
            Log.d("XDFCE", "location callback received")
            if (p0 != null && p0.lastLocation != null) {
                Log.d("XDFCE", "location callback valid")
                locationContext = LocationContext(p0.lastLocation.latitude, p0.lastLocation.longitude)
                contextChangeCallback?.invoke()
            }
        }
    }

    @RequiresPermission(value = Manifest.permission.ACCESS_FINE_LOCATION)
    fun startLocationUpdates() {

        val locationRequest = LocationRequest()
        locationRequest.fastestInterval = 10000 / 2
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun stopListeningToContextChanges() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}
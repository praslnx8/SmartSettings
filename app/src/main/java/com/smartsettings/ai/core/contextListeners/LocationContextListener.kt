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
import com.smartsettings.ai.core.contextListeners.contextData.LocationContext
import com.smartsettings.ai.core.serializables.SerializableData
import com.smartsettings.ai.data.criteriaData.LocationData
import com.smartsettings.ai.di.DependencyProvider
import com.smartsettings.ai.utils.LocationUtils
import com.smartsettings.permissionhelper.PermissionManager


class LocationContextListener(locationData: LocationData) :
    ContextListener<LocationData>(SerializableData(locationData)) {

    val context: Context = DependencyProvider.getContext

    private var locationContext: LocationContext? = null

    private val fusedLocationProviderClient: FusedLocationProviderClient = DependencyProvider.fusedLocationProviderClient

    @SuppressLint("MissingPermission")
    override fun startListeningToContextChanges() {
        startLocationUpdates()
    }

    override fun isListeningPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        } else {
            ContextCompat.checkSelfPermission(
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
                locationContext = LocationContext(p0.lastLocation.latitude, p0.lastLocation.longitude)
                onContextChange()
            }
        }
    }

    @RequiresPermission(value = Manifest.permission.ACCESS_FINE_LOCATION)
    private fun startLocationUpdates() {

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

    override fun isCriteriaMatches(criteriaData: LocationData): Boolean {
        if (LocationUtils.getDistanceInMetre(
                Pair(locationContext?.lat ?: 0.0, locationContext?.lon ?: 0.0),
                Pair(criteriaData.lat, criteriaData.lon)
            ) < criteriaData.radiusInMetre
        ) {
            Log.d("XDFCE", "criteria matched")
            return true
        }

        return false
    }

    override fun stopListeningToContextChanges() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}
package com.smartsettings.ai.core.contextListeners

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.TestAppModule
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito

class LocationContextListenerTest {

    private val testAppModule = TestAppModule()

    @Before
    fun setup() {
        SmartApp.setDaggerComponentForTesting(testAppModule)
    }

    @Test
    fun check_context_not_null() {
        Assert.assertNotNull(LocationContextListener().context)
    }

    @Test
    fun start_listening_to_context_should_call_fused_location_provider() {
        LocationContextListener().startLocationUpdates()

        val locationRequest = LocationRequest()
        locationRequest.fastestInterval = 10000 / 2
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        Mockito.verify(testAppModule.fusedLocationProviderClient)
            .requestLocationUpdates(eq(locationRequest), any(LocationCallback::class.java), eq(Looper.getMainLooper()))
    }

    @Test
    fun check_permission_should_call_with_true_if_permission_is_enabled() {

        var isPermissionGranted = false

        Mockito.`when`(
            testAppModule.provideContext().checkPermission(
                eq(Manifest.permission.ACCESS_FINE_LOCATION),
                anyInt(),
                anyInt()
            )
        ).thenReturn(PackageManager.PERMISSION_GRANTED)

        LocationContextListener().askListeningPermission {
            isPermissionGranted = it
        }

        assert(isPermissionGranted)
    }
}
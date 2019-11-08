package com.smartsettings.ai.core.contextListeners

import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.TestAppModule
import com.smartsettings.ai.data.criteriaData.LocationData
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito
import org.mockito.Mockito.mock

class LocationContextListenerTest {

    private val testAppModule = TestAppModule()

    @Before
    fun setup() {
        SmartApp.setDaggerComponentForTesting(testAppModule)
    }

    @Test
    fun check_context_not_null() {
        val locationData = mock(LocationData::class.java)
        Assert.assertNotNull(LocationContextListener(locationData).context)
    }

    @Test
    fun start_listening_to_context_should_call_fused_location_provider() {
        val locationData = mock(LocationData::class.java)
        LocationContextListener(locationData).startListeningToContextChanges {

        }

        val locationRequest = LocationRequest()
        locationRequest.fastestInterval = 10000 / 2
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        Mockito.verify(testAppModule.fusedLocationProviderClient)
            .requestLocationUpdates(eq(locationRequest), any(LocationCallback::class.java), eq(Looper.getMainLooper()))
    }
}
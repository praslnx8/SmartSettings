package com.smartsettings.ai.core.contextListeners

import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.smartsettings.ai.TestAppInjector
import com.smartsettings.ai.data.criteriaData.LocationData
import com.smartsettings.ai.di.DependencyProvider
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito
import org.mockito.Mockito.mock

class LocationContextListenerTest {

    val mockFusedLocationProviderClient = mock(FusedLocationProviderClient::class.java)

    @Before
    fun setup() {
        DependencyProvider.setInjector(object : TestAppInjector() {
            override fun provideFusedLocationProviderClient(): FusedLocationProviderClient {
                return mockFusedLocationProviderClient
            }

            override fun provideContext(): Context {
                return mock(Context::class.java)
            }
        })
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


        Mockito.verify(mockFusedLocationProviderClient)
            .requestLocationUpdates(eq(locationRequest), any(LocationCallback::class.java), eq(Looper.getMainLooper()))
    }
}
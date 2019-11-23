package com.smartsettings.ai.core.contextListeners

import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.smartsettings.ai.TestAppInjector
import com.smartsettings.ai.data.criteriaData.LocationData
import com.smartsettings.ai.di.DependencyProvider
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class LocationContextListenerTest {

    val mockFusedLocationProviderClient: FusedLocationProviderClient = mock(FusedLocationProviderClient::class.java)
    val mockContext : Context = mock(Context::class.java)

    @Before
    fun setup() {
        DependencyProvider.setInjector(object : TestAppInjector() {
            override fun provideFusedLocationProviderClient(): FusedLocationProviderClient {
                return mockFusedLocationProviderClient
            }

            override fun provideContext(): Context {
                return mockContext
            }
        })
    }

    @Test
    fun check_context_not_null() {
        val locationData = mock(LocationData::class.java)
        Assert.assertNotNull(LocationContextListener(locationData).context)
    }


    @Test
    fun check_is_listening_permission_granted_returns_true() {
        val locationData = mock(LocationData::class.java)

        Mockito.`when`(mockContext.checkPermission(any(), any(), any())).thenReturn(PackageManager.PERMISSION_GRANTED)

        assert(LocationContextListener(locationData).isListeningPermissionGranted ())
    }


    @Test
    fun start_listening_to_context_should_call_fused_location_provider() {
        val locationData = mock(LocationData::class.java)
        LocationContextListener(locationData).startListeningToContextChanges ({},{})

        val locationRequest = LocationRequest()
        locationRequest.fastestInterval = 10000 / 2
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY


        Mockito.verify(mockFusedLocationProviderClient)
            .requestLocationUpdates(eq(locationRequest), any(), eq(Looper.getMainLooper()))
    }
}
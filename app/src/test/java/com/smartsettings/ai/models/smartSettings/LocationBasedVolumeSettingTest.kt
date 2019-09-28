package com.smartsettings.ai.models.smartSettings

import android.content.Context
import android.media.AudioManager
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.whenever
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.models.actionData.VolumeActionData
import com.smartsettings.ai.models.contextData.LocationContext
import com.smartsettings.ai.models.contextListeners.CurrentLocationListener
import com.smartsettings.ai.models.criteriaData.LocationData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


class LocationBasedVolumeSettingTest {

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var audioManager: AudioManager

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var currentLocationListener: CurrentLocationListener

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        Mockito.`when`(context.getSystemService(Context.AUDIO_SERVICE)).thenReturn(audioManager)

        SmartApp.setDaggerComponentForTesting(context)
    }

    @Test
    fun check_criteria_should_return_true_for_valid_location_change() {

        val criteriaData = LocationData(12.12, 80.80, 10000)
        val actionData = VolumeActionData(80)

        val locationBasedVolumeSetting = LocationBasedVolumeSetting(criteriaData, actionData)
        val changedData = LocationContext(12.12, 80.80)

        whenever(locationBasedVolumeSetting.currentLocationListener.askListeningPermissionIfAny { }).thenAnswer {
            val callBack = it.getArgument<((Boolean) -> Unit)>(0)
            callBack.invoke(true)
        }

        Mockito.`when`(locationBasedVolumeSetting.currentLocationListener.startListeningToContextChanges(any()))
            .thenAnswer {
                val callBack = it.getArgument<((LocationContext) -> Unit)>(0)
                callBack.invoke(changedData)
            }

        locationBasedVolumeSetting.setEnabled(true)
        locationBasedVolumeSetting.start()

        Mockito.verify(audioManager, times(1))
            .setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 80)
    }
}
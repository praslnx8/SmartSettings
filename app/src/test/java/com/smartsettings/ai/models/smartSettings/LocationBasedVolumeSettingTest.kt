package com.smartsettings.ai.models.smartSettings

import android.content.Context
import android.media.AudioManager
import com.nhaarman.mockitokotlin2.times
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.models.changedData.LocationData
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

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        Mockito.`when`(context.getSystemService(Context.AUDIO_SERVICE)).thenReturn(audioManager)

        SmartApp.setDaggerComponentForTesting(context)
    }

    @Test
    fun check_criteria_should_return_true_for_valid_location_change() {

        val locationBasedVolumeSetting = LocationBasedVolumeSetting(12.12, 80.80, 10000, 80)
        val changedData = LocationData(12.12, 80.80)

        locationBasedVolumeSetting.onChange(changedData)

        Mockito.verify(audioManager, times(1))
            .setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 80)
    }
}
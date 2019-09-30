package com.smartsettings.ai.models.smartSettings

import android.content.Context
import android.media.AudioManager
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.TestAppModule
import com.smartsettings.ai.models.actionData.VolumeActionData
import com.smartsettings.ai.models.contextData.LocationContext
import com.smartsettings.ai.models.criteriaData.LocationData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


class LocationBasedVolumeSettingTest {

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    private val testAppModule = TestAppModule()

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        SmartApp.setDaggerComponentForTesting(testAppModule)

        Mockito.`when`(testAppModule.provideContext().getSystemService(Context.AUDIO_SERVICE))
            .thenReturn(testAppModule.audioManager)

    }

    @Test
    fun check_criteria_should_return_true_for_valid_location_change() {

        val criteriaData = LocationData(12.12, 80.80, 10000)
        val actionData = VolumeActionData(80)

        val locationBasedVolumeSetting = LocationBasedVolumeSetting("", criteriaData, actionData)
        val changedData = LocationContext(12.12, 80.80)

        locationBasedVolumeSetting.setEnabled(true)

        Mockito.`when`(testAppModule.currentLocationListener.askListeningPermissionIfAny(any())).then {
            it.getArgument<((Boolean) -> Unit)>(0)(true)
        }

        Mockito.`when`(testAppModule.currentLocationListener.startListeningToContextChanges(any())).then {
            it.getArgument<((LocationContext) -> Unit)>(0)(changedData)
        }

        locationBasedVolumeSetting.start()

        Mockito.verify(testAppModule.audioManager, times(1))
            .setStreamVolume(
                AudioManager.STREAM_RING,
                testAppModule.audioManager.getStreamMaxVolume(AudioManager.STREAM_RING),
                80
            )
    }
}
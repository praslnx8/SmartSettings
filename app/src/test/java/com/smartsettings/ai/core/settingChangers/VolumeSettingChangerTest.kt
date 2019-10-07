package com.smartsettings.ai.core.settingChangers

import android.content.Context
import android.media.AudioManager
import com.nhaarman.mockitokotlin2.times
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.TestAppModule
import com.smartsettings.ai.data.actionData.VolumeActionData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class VolumeSettingChangerTest {


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
    fun enable_smart_setting_should_set_true_against_setting() {

        val volumeSettingChanger = VolumeSettingChanger(VolumeActionData(10))

        volumeSettingChanger.applyChanges()

        Mockito.verify(testAppModule.audioManager, times(1))
            .setStreamVolume(
                AudioManager.STREAM_RING,
                testAppModule.audioManager.getStreamMaxVolume(AudioManager.STREAM_RING),
                10
            )
    }
}
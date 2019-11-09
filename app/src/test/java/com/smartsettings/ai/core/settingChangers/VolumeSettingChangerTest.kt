package com.smartsettings.ai.core.settingChangers

import android.content.Context
import android.media.AudioManager
import com.nhaarman.mockitokotlin2.times
import com.smartsettings.ai.TestAppInjector
import com.smartsettings.ai.data.actionData.VolumeActionData
import com.smartsettings.ai.di.DependencyProvider
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

    val context = Mockito.mock(Context::class.java)
    val audioManager = Mockito.mock(AudioManager::class.java)


    @Before
    fun setUp() {

        DependencyProvider.setInjector(object : TestAppInjector() {
            override fun provideContext(): Context {
                return context
            }

            override fun provideAudioManager(): AudioManager {
                return audioManager
            }
        })

        MockitoAnnotations.initMocks(this)

        Mockito.`when`(context.getSystemService(Context.AUDIO_SERVICE))
            .thenReturn(audioManager)

    }

    @Test
    fun enable_smart_setting_should_set_true_against_setting() {

        val volumeSettingChanger = VolumeSettingChanger(VolumeActionData(10))

        volumeSettingChanger.applyChanges()

        Mockito.verify(audioManager, times(1))
            .setStreamVolume(
                AudioManager.STREAM_RING,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_RING),
                10
            )
    }
}
package com.smartsettings.ai.core.smartSettings

import android.content.Context
import com.nhaarman.mockitokotlin2.any
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.TestAppModule
import com.smartsettings.ai.core.settingChangers.VolumeSettingChanger
import com.smartsettings.ai.data.contextData.LocationContext
import com.smartsettings.ai.data.criteriaData.LocationData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


class LocationBasedSmartSettingTest {

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
    fun correct_context_should_trigger_setting_changer_apply_changes() {

        val criteriaData = LocationData(12.12, 80.80, 10000)

        val locationBasedVolumeSetting = LocationBasedSmartSetting("", criteriaData)
        val changedData = LocationContext(12.12, 80.80)

        val volumeSettingMock = Mockito.mock(VolumeSettingChanger::class.java)

        locationBasedVolumeSetting.settingChangers.add(volumeSettingMock)
        locationBasedVolumeSetting.setEnabled(true)

        Mockito.`when`(testAppModule.currentLocationListener.askListeningPermissionIfAny(any())).then {
            it.getArgument<((Boolean) -> Unit)>(0)(true)
        }

        Mockito.`when`(testAppModule.currentLocationListener.startListeningToContextChanges(any())).then {
            it.getArgument<(() -> Unit)>(0)()
        }

        Mockito.`when`(volumeSettingMock.askSettingChangePermissionIfAny(any())).then {
            it.getArgument<((Boolean) -> Unit)>(0)(true)
        }

        Mockito.`when`(testAppModule.currentLocationListener.getContextData())
            .thenReturn(changedData)

        locationBasedVolumeSetting.start()

        Mockito.verify(volumeSettingMock).applyChanges()
    }
}
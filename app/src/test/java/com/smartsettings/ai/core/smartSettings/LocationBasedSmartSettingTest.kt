package com.smartsettings.ai.core.smartSettings

import android.content.Context
import com.nhaarman.mockitokotlin2.any
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.TestAppModule
import com.smartsettings.ai.core.contextListeners.LocationContextListener
import com.smartsettings.ai.core.settingChangers.VolumeSettingChanger
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
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

        val contextListener = mock(LocationContextListener::class.java)
        val settingChanger = mock(VolumeSettingChanger::class.java)

        val locationBasedVolumeSetting =
            SmartSetting(null, "", setOf(contextListener), setOf(settingChanger), SmartSetting.AND)

        locationBasedVolumeSetting.setEnabled(true)

        Mockito.`when`(contextListener.askListeningPermissionIfAny(any())).then {
            it.getArgument<((Boolean) -> Unit)>(0)(true)
        }

        Mockito.`when`(contextListener.startListeningToContextChanges(any())).then {
            it.getArgument<(() -> Unit)>(0)()
        }

        Mockito.`when`(settingChanger.askSettingChangePermissionIfAny(any())).then {
            it.getArgument<((Boolean) -> Unit)>(0)(true)
        }

        Mockito.`when`(contextListener.isCriteriaMatches())
            .thenReturn(true)

        locationBasedVolumeSetting.start()

        Mockito.verify(settingChanger).applyChanges()
    }
}
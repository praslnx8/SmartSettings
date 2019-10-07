package com.smartsettings.ai.dagger

import com.smartsettings.ai.MainActivity
import com.smartsettings.ai.core.SmartProfile
import com.smartsettings.ai.core.SmartSettingRepository
import com.smartsettings.ai.core.contextListeners.CurrentLocationListener
import com.smartsettings.ai.core.settingChangers.VolumeSettingChanger
import com.smartsettings.ai.core.smartSettings.LocationBasedVolumeSetting
import com.smartsettings.ai.uiModules.home.HomeViewModel
import com.smartsettings.ai.uiModules.smartSettingsChooser.SmartSettingsChooserViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Exclude test case for this module.
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(target: MainActivity)

    fun inject(target: LocationBasedVolumeSetting)

    fun inject(target: SmartSettingRepository)

    fun inject(target: HomeViewModel)

    fun inject(target: SmartSettingsChooserViewModel)

    fun inject(target: CurrentLocationListener)

    fun inject(target: SmartProfile)

    fun inject(target: VolumeSettingChanger)
}
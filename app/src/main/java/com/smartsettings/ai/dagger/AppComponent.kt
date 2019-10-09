package com.smartsettings.ai.dagger

import com.smartsettings.ai.MainActivity
import com.smartsettings.ai.core.SmartProfile
import com.smartsettings.ai.core.SmartSettingRepository
import com.smartsettings.ai.core.contextListeners.CurrentLocationListener
import com.smartsettings.ai.core.contextListeners.WifiListener
import com.smartsettings.ai.core.settingChangers.VolumeSettingChanger
import com.smartsettings.ai.core.smartSettings.LocationBasedSmartSetting
import com.smartsettings.ai.core.smartSettings.WifiBasedSmartSetting
import com.smartsettings.ai.uiModules.home.HomeViewModel
import com.smartsettings.ai.uiModules.smartSettingCreator.SmartSettingCreatorViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Exclude test case for this module.
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(target: MainActivity)

    fun inject(target: LocationBasedSmartSetting)

    fun inject(target: SmartSettingRepository)

    fun inject(target: HomeViewModel)

    fun inject(target: CurrentLocationListener)

    fun inject(target: SmartProfile)

    fun inject(target: VolumeSettingChanger)

    fun inject(target: SmartSettingCreatorViewModel)

    fun inject(target: WifiBasedSmartSetting)

    fun inject(target: WifiListener)
}
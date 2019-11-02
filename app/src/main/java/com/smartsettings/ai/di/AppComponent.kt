package com.smartsettings.ai.di

import com.smartsettings.ai.MainActivity
import com.smartsettings.ai.core.SmartProfile
import com.smartsettings.ai.core.SmartSettingRepository
import com.smartsettings.ai.core.contextListeners.LocationContextListener
import com.smartsettings.ai.core.contextListeners.WifiContextListener
import com.smartsettings.ai.core.settingChangers.VolumeSettingChanger
import com.smartsettings.ai.core.smartSettingCreator.SmartSettingCreator
import com.smartsettings.ai.core.smartSettingCreator.SmartSettingSchemaRepo
import com.smartsettings.ai.uiModules.smartSettingCreatorView.SmartSettingCreatorViewModel
import com.smartsettings.ai.uiModules.smartSettingView.SmartSettingViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Exclude test case for this module.
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(target: MainActivity)

    fun inject(target: SmartSettingRepository)

    fun inject(target: SmartSettingViewModel)

    fun inject(target: LocationContextListener)

    fun inject(target: SmartProfile)

    fun inject(target: VolumeSettingChanger)

    fun inject(target: WifiContextListener)

    fun inject(smartSettingSchemaRepo: SmartSettingSchemaRepo)

    fun inject(smartSettingCreator: SmartSettingCreator)

    fun inject(smartSettingCreatorViewModel: SmartSettingCreatorViewModel)
}
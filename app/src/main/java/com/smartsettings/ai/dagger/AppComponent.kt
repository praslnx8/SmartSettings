package com.smartsettings.ai.dagger

import com.smartsettings.ai.MainActivity
import com.smartsettings.ai.models.SmartProfile
import com.smartsettings.ai.models.contextListeners.CurrentLocationListener
import com.smartsettings.ai.models.smartSettings.LocationBasedVolumeSetting
import com.smartsettings.ai.repositories.SmartSettingRepository
import com.smartsettings.ai.uiModules.home.HomeViewModel
import com.smartsettings.ai.uiModules.smartSettingsChooser.SmartSettingsChooserViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, DaoModule::class, RepoModule::class, SettingsModule::class])
interface AppComponent {

    fun inject(target: MainActivity)

    fun inject(target: LocationBasedVolumeSetting)

    fun inject(target: SmartSettingRepository)

    fun inject(target: HomeViewModel)

    fun inject(target: SmartSettingsChooserViewModel)

    fun inject(target: CurrentLocationListener)

    fun inject(target: SmartProfile)
}
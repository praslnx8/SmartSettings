package com.smartsettings.ai.dagger

import com.smartsettings.ai.MainActivity
import com.smartsettings.ai.models.smartSettings.LocationBasedVolumeSetting
import com.smartsettings.ai.modules.home.HomeViewModel
import com.smartsettings.ai.repositories.SmartSettingRepository
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, DaoModule::class, RepoModule::class])
interface AppComponent {

    fun inject(target: MainActivity)

    fun inject(target: LocationBasedVolumeSetting)

    fun inject(target: SmartSettingRepository)

    fun inject(target: HomeViewModel)
}
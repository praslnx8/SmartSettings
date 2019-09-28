package com.smartsettings.ai

import com.smartsettings.ai.dagger.AppComponent
import com.smartsettings.ai.models.smartSettings.LocationBasedVolumeSettingTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestAppModule::class])
interface TestAppComponent : AppComponent {
    fun inject(target: LocationBasedVolumeSettingTest)
}
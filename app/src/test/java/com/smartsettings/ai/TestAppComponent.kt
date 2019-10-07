package com.smartsettings.ai

import com.smartsettings.ai.core.smartSettings.LocationBasedVolumeSettingTest
import com.smartsettings.ai.dagger.AppComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestAppModule::class])
interface TestAppComponent : AppComponent {
    fun inject(target: LocationBasedVolumeSettingTest)
}
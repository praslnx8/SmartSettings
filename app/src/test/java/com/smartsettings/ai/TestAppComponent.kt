package com.smartsettings.ai

import com.smartsettings.ai.core.smartSettings.LocationBasedSmartSettingTest
import com.smartsettings.ai.di.AppComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestAppModule::class])
interface TestAppComponent : AppComponent {
    fun inject(target: LocationBasedSmartSettingTest)
}
package com.smartsettings.ai.dagger

import com.smartsettings.ai.models.contextListeners.CurrentLocationListener
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SettingsModule {

    @Provides
    @Singleton
    fun provideCurrentLocationListener(): CurrentLocationListener {
        return CurrentLocationListener()
    }
}
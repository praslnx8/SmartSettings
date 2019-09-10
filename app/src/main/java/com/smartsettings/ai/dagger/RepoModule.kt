package com.smartsettings.ai.dagger

import com.smartsettings.ai.repositories.SmartSettingRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepoModule {

    @Provides
    @Singleton
    fun provideSmartSettingsRepo(): SmartSettingRepository {
        return SmartSettingRepository()
    }
}
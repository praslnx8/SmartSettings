package com.smartsettings.ai

import android.content.Context
import android.media.AudioManager
import com.smartsettings.ai.models.contextListeners.CurrentLocationListener
import com.smartsettings.ai.repositories.SmartSettingRepository
import com.smartsettings.ai.resources.db.SmartSettingDao
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock
import javax.inject.Singleton

@Module
class TestAppModule {

    @Provides
    @Singleton
    fun provideContext(): Context = mock(Context::class.java)

    @Provides
    @Singleton
    fun provideAudioManager(): AudioManager = mock(AudioManager::class.java)

    @Provides
    @Singleton
    fun provideSmartSettingDao(): SmartSettingDao {
        return mock(SmartSettingDao::class.java)
    }

    @Provides
    @Singleton
    fun provideSmartSettingsRepo(): SmartSettingRepository {
        return mock(SmartSettingRepository::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrentLocationListener(): CurrentLocationListener {
        return mock(CurrentLocationListener::class.java)
    }
}
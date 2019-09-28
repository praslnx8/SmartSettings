package com.smartsettings.ai.dagger

import android.content.Context
import android.media.AudioManager
import com.smartsettings.ai.models.contextListeners.CurrentLocationListener
import com.smartsettings.ai.repositories.SmartSettingRepository
import com.smartsettings.ai.resources.db.SmartSettingDao
import com.smartsettings.ai.resources.db.SmartSettingDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideAudioManager(): AudioManager = app.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    @Provides
    @Singleton
    fun provideSmartSettingDao(): SmartSettingDao {
        return SmartSettingDatabase.getDb(app).getSmartSettingDao()
    }

    @Provides
    @Singleton
    fun provideSmartSettingsRepo(): SmartSettingRepository {
        return SmartSettingRepository()
    }

    @Provides
    @Singleton
    fun provideCurrentLocationListener(): CurrentLocationListener {
        return CurrentLocationListener()
    }
}
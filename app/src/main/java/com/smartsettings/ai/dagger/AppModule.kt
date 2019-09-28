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
open class AppModule(private val app: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    open fun provideAudioManager(): AudioManager = app.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    @Provides
    @Singleton
    open fun provideSmartSettingDao(): SmartSettingDao {
        return SmartSettingDatabase.getDb(app).getSmartSettingDao()
    }

    @Provides
    @Singleton
    open fun provideSmartSettingsRepo(): SmartSettingRepository {
        return SmartSettingRepository()
    }

    @Provides
    @Singleton
    open fun provideCurrentLocationListener(): CurrentLocationListener {
        return CurrentLocationListener()
    }
}
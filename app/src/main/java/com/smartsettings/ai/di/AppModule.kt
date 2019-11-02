package com.smartsettings.ai.di

import android.content.Context
import android.media.AudioManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.smartsettings.ai.core.SmartSettingRepository
import com.smartsettings.ai.core.smartSettingCreator.SmartSettingCreator
import com.smartsettings.ai.core.smartSettingCreator.SmartSettingSchemaRepo
import com.smartsettings.ai.resources.cloud.ApiService
import com.smartsettings.ai.resources.cloud.ApiServiceProvider
import com.smartsettings.ai.resources.db.SmartSettingDao
import com.smartsettings.ai.resources.db.SmartSettingDatabase
import com.smartsettings.ai.resources.db.SmartSettingSchemaDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Exclude test case for this module.
 */
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
    open fun provideSmartSettingSchemaDao(): SmartSettingSchemaDao {
        return SmartSettingDatabase.getDb(app).getSmartSettingSchemaDao()
    }

    @Provides
    @Singleton
    open fun provideSmartSettingsRepo(): SmartSettingRepository {
        return SmartSettingRepository()
    }

    @Provides
    open fun provideFusedLocationProviderClient(): FusedLocationProviderClient {
        return FusedLocationProviderClient(app)
    }

    @Provides
    @Singleton
    open fun provideApiService(): ApiService {
        return ApiServiceProvider.apiService
    }

    @Provides
    @Singleton
    open fun provideSmartSettingSchemaRepo(): SmartSettingSchemaRepo {
        return SmartSettingSchemaRepo()
    }

    @Provides
    @Singleton
    open fun provideSmartSettingCreator(): SmartSettingCreator {
        return SmartSettingCreator()
    }
}
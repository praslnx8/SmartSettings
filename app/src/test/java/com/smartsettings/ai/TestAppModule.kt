package com.smartsettings.ai

import android.content.Context
import android.media.AudioManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.smartsettings.ai.core.SmartSettingRepository
import com.smartsettings.ai.di.AppModule
import com.smartsettings.ai.resources.cloud.ApiService
import com.smartsettings.ai.resources.db.SmartSettingDao
import com.smartsettings.ai.resources.db.SmartSettingSchemaDao
import org.mockito.Mockito.mock

class TestAppModule : AppModule(mock(Context::class.java)) {

    val audioManager: AudioManager = mock(AudioManager::class.java)
    val smartSettingDao: SmartSettingDao = mock(SmartSettingDao::class.java)
    val smartSettingSchemaDao: SmartSettingSchemaDao = mock(SmartSettingSchemaDao::class.java)
    val smartSettingRepository: SmartSettingRepository = mock(SmartSettingRepository::class.java)
    val fusedLocationProviderClient: FusedLocationProviderClient = mock(FusedLocationProviderClient::class.java)
    val apiService: ApiService = mock(ApiService::class.java)

    override fun provideAudioManager(): AudioManager = audioManager

    override fun provideSmartSettingDao(): SmartSettingDao {
        return smartSettingDao
    }

    override fun provideSmartSettingsRepo(): SmartSettingRepository {
        return smartSettingRepository
    }

    override fun provideSmartSettingSchemaDao(): SmartSettingSchemaDao {
        return smartSettingSchemaDao
    }

    override fun provideApiService(): ApiService {
        return apiService
    }

    override fun provideFusedLocationProviderClient(): FusedLocationProviderClient {
        return fusedLocationProviderClient
    }
}
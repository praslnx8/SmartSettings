package com.smartsettings.ai

import android.content.Context
import android.media.AudioManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.smartsettings.ai.dagger.AppModule
import com.smartsettings.ai.models.contextListeners.CurrentLocationListener
import com.smartsettings.ai.repositories.SmartSettingRepository
import com.smartsettings.ai.resources.db.SmartSettingDao
import org.mockito.Mockito.mock

class TestAppModule : AppModule(mock(Context::class.java)) {

    val audioManager: AudioManager = mock(AudioManager::class.java)
    val smartSettingDao: SmartSettingDao = mock(SmartSettingDao::class.java)
    val smartSettingRepository: SmartSettingRepository = mock(SmartSettingRepository::class.java)
    val currentLocationListener: CurrentLocationListener = mock(CurrentLocationListener::class.java)
    val fusedLocationProviderClient: FusedLocationProviderClient = mock(FusedLocationProviderClient::class.java)

    override fun provideAudioManager(): AudioManager = audioManager

    override fun provideSmartSettingDao(): SmartSettingDao {
        return smartSettingDao
    }

    override fun provideSmartSettingsRepo(): SmartSettingRepository {
        return smartSettingRepository
    }

    override fun provideCurrentLocationListener(): CurrentLocationListener {
        return currentLocationListener
    }

    override fun provideFusedLocationProviderClient(): FusedLocationProviderClient {
        return fusedLocationProviderClient
    }
}
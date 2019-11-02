package com.smartsettings.ai

import android.content.Context
import android.media.AudioManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.smartsettings.ai.core.SmartSettingRepository
import com.smartsettings.ai.core.contextListeners.LocationContextListener
import com.smartsettings.ai.di.AppModule
import com.smartsettings.ai.resources.db.SmartSettingDao
import org.mockito.Mockito.mock

class TestAppModule : AppModule(mock(Context::class.java)) {

    val audioManager: AudioManager = mock(AudioManager::class.java)
    val smartSettingDao: SmartSettingDao = mock(SmartSettingDao::class.java)
    val smartSettingRepository: SmartSettingRepository = mock(SmartSettingRepository::class.java)
    val locationContextListener: LocationContextListener = mock(LocationContextListener::class.java)
    val fusedLocationProviderClient: FusedLocationProviderClient = mock(FusedLocationProviderClient::class.java)

    override fun provideAudioManager(): AudioManager = audioManager

    override fun provideSmartSettingDao(): SmartSettingDao {
        return smartSettingDao
    }

    override fun provideSmartSettingsRepo(): SmartSettingRepository {
        return smartSettingRepository
    }

    override fun provideCurrentLocationListener(): LocationContextListener {
        return locationContextListener
    }

    override fun provideFusedLocationProviderClient(): FusedLocationProviderClient {
        return fusedLocationProviderClient
    }
}
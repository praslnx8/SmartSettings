package com.smartsettings.ai

import android.content.Context
import android.media.AudioManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.smartsettings.ai.core.SmartSettingRepository
import com.smartsettings.ai.core.smartSettingCreator.SmartSettingCreator
import com.smartsettings.ai.core.smartSettingSchemaProvider.SmartSettingSchemaProvider
import com.smartsettings.ai.core.smartSettingSchemaProvider.SmartSettingSchemaRepo
import com.smartsettings.ai.di.AbstractDependencyInjector
import com.smartsettings.ai.resources.cloud.ApiService
import com.smartsettings.ai.resources.db.SmartSettingDao
import com.smartsettings.ai.resources.db.SmartSettingSchemaDao

abstract class TestAppInjector : AbstractDependencyInjector() {
    override fun provideContext(): Context {
        throw IllegalArgumentException("Should be overrided by child class")
    }

    override fun provideAudioManager(): AudioManager {
        throw IllegalArgumentException("Should be overrided by child class")
    }

    override fun provideSmartSettingRepository(): SmartSettingRepository {
        throw IllegalArgumentException("Should be overrided by child class")
    }

    override fun provideFusedLocationProviderClient(): FusedLocationProviderClient {
        throw IllegalArgumentException("Should be overrided by child class")
    }

    override fun provideApiService(): ApiService {
        throw IllegalArgumentException("Should be overrided by child class")
    }

    override fun provideSmartSettingSchemaRepo(): SmartSettingSchemaRepo {
        throw IllegalArgumentException("Should be overrided by child class")
    }

    override fun provideSmartSettingCreator(): SmartSettingCreator {
        throw IllegalArgumentException("Should be overrided by child class")
    }

    override fun provideSmartSettingDao(): SmartSettingDao {
        throw IllegalArgumentException("Should be overrided by child class")
    }

    override fun provideSmartSettingSchemaDao(): SmartSettingSchemaDao {
        throw IllegalArgumentException("Should be overrided by child class")
    }

    override fun provideSmartSettingSchemaProvider(): SmartSettingSchemaProvider {
        throw IllegalArgumentException("Should be overrided by child class")
    }
}
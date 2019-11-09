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

object DependencyProvider {

    private lateinit var abstractDependencyInjector: AbstractDependencyInjector

    fun setInjector(abstractDependencyInjector: AbstractDependencyInjector) {
        this.abstractDependencyInjector = abstractDependencyInjector
    }

    val getContext : Context get() = abstractDependencyInjector.provideContext()

    val audioManager : AudioManager get() = abstractDependencyInjector.provideAudioManager()

    val smartSettingRepository : SmartSettingRepository get() = abstractDependencyInjector.provideSmartSettingRepository()

    val fusedLocationProviderClient : FusedLocationProviderClient get() = abstractDependencyInjector.provideFusedLocationProviderClient()

    val apiService : ApiService get() = abstractDependencyInjector.provideApiService()

    val smartSettingSchemaRepo : SmartSettingSchemaRepo get() = abstractDependencyInjector.provideSmartSettingSchemaRepo()

    val smartSettingCreator : SmartSettingCreator get() = abstractDependencyInjector.provideSmartSettingCreator()

    val smartSettingDao : SmartSettingDao get() = abstractDependencyInjector.provideSmartSettingDao()

    val smartSettingSchemaDao : SmartSettingSchemaDao get() = abstractDependencyInjector.provideSmartSettingSchemaDao()
}

abstract class AbstractDependencyInjector {

    abstract fun provideContext() : Context

    abstract fun provideAudioManager() : AudioManager

    abstract fun provideSmartSettingRepository() : SmartSettingRepository

    abstract fun provideFusedLocationProviderClient() : FusedLocationProviderClient

    abstract fun provideApiService() : ApiService

    abstract fun provideSmartSettingSchemaRepo() : SmartSettingSchemaRepo

    abstract fun provideSmartSettingCreator() : SmartSettingCreator

    abstract fun provideSmartSettingDao() : SmartSettingDao

    abstract fun provideSmartSettingSchemaDao() : SmartSettingSchemaDao
}

class DependencyInjector(val appContext: Context) : AbstractDependencyInjector() {

    private val context : Context by lazy {
        appContext
    }

    private val audioManager : AudioManager by lazy {
        appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    private val smartSettingDao : SmartSettingDao by lazy {
        SmartSettingDatabase.getDb(appContext).getSmartSettingDao()
    }

    private val smartSettingSchemaDao : SmartSettingSchemaDao by lazy {
        SmartSettingDatabase.getDb(appContext).getSmartSettingSchemaDao()
    }

    private val smartSettingRepository : SmartSettingRepository by lazy {
        SmartSettingRepository()
    }

    private val fusedLocationProviderClient : FusedLocationProviderClient get() = FusedLocationProviderClient(appContext)

    private val apiService : ApiService by lazy {
        ApiServiceProvider.apiService
    }

    private val smartSettingSchemaRepo : SmartSettingSchemaRepo by lazy {
        SmartSettingSchemaRepo()
    }

    private val smartSettingCreator : SmartSettingCreator by lazy {
        SmartSettingCreator()
    }

    override fun provideContext(): Context {
        return context
    }

    override fun provideAudioManager(): AudioManager {
        return audioManager
    }

    override fun provideSmartSettingRepository(): SmartSettingRepository {
        return smartSettingRepository
    }

    override fun provideFusedLocationProviderClient(): FusedLocationProviderClient {
        return fusedLocationProviderClient
    }

    override fun provideApiService(): ApiService {
        return apiService
    }

    override fun provideSmartSettingSchemaRepo(): SmartSettingSchemaRepo {
        return smartSettingSchemaRepo
    }

    override fun provideSmartSettingCreator(): SmartSettingCreator {
        return smartSettingCreator
    }

    override fun provideSmartSettingDao(): SmartSettingDao {
        return smartSettingDao
    }

    override fun provideSmartSettingSchemaDao(): SmartSettingSchemaDao {
        return smartSettingSchemaDao
    }
}

package com.smartsettings.ai.dagger

import android.content.Context
import com.smartsettings.ai.resources.db.SmartSettingDao
import com.smartsettings.ai.resources.db.SmartSettingDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaoModule(private val app: Context) {

    @Provides
    @Singleton
    fun provideSmartSettingDao(): SmartSettingDao {
        return SmartSettingDatabase.getDb(app).getSmartSettingDao()
    }
}
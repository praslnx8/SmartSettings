package com.smartsettings.ai.resources.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SmartSettingDBModel::class, SmartSettingSchemaDBModel::class], version = 1)
@TypeConverters(SettingChangerConverter::class, ContextListenerConverter::class, ListConverter::class, CategoryConverter::class, ContextListenerSchemasConverter::class, SettingChangerSchemasConverter::class)
abstract class SmartSettingDatabase : RoomDatabase() {

    abstract fun getSmartSettingDao(): SmartSettingDao

    abstract fun getSmartSettingSchemaDao(): SmartSettingSchemaDao

    companion object {
        @Volatile
        private var INSTANCE: SmartSettingDatabase? = null

        fun getDb(context: Context): SmartSettingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartSettingDatabase::class.java,
                    "smart_setting_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
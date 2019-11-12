package com.smartsettings.ai.resources.db

import androidx.room.*

@Dao
interface SmartSettingDao {

    @Query("SELECT * FROM smart_settings")
    fun getSmartSettings(): List<SmartSettingDBModel>

    @Query("SELECT * FROM smart_settings WHERE name = :name")
    fun getSmartSettingByName(name: String): SmartSettingDBModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSmartSetting(smartSettingDBModel: SmartSettingDBModel): Long

    @Update
    fun updateSmartSetting(smartSettingDBModel: SmartSettingDBModel)

    @Delete
    fun deleteSmartSetting(smartSettingDBModel: SmartSettingDBModel)
}

@Dao
interface SmartSettingSchemaDao {

    @Query("SELECT * from smart_setting_schema")
    fun getSmartSettingSchemas(): List<SmartSettingSchemaDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSmartSetting(smartSettingSchemaDBModels: List<SmartSettingSchemaDBModel>)

    @Delete
    fun deleteAll()
}
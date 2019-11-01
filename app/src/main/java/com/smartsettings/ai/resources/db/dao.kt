package com.smartsettings.ai.resources.db

import androidx.room.*

@Dao
interface SmartSettingDao {

    @Query("SELECT * FROM smart_settings")
    fun getSmartSettings(): List<SmartSettingDBModel>

    @Query("SELECT * FROM smart_settings WHERE name = :name")
    fun getSmartSettingByName(name: String): SmartSettingDBModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSmartSetting(smartSettingDBModel: SmartSettingDBModel)

    @Update
    fun updateSmartSetting(smartSettingDBModel: SmartSettingDBModel)

    @Delete
    fun deleteSmartSetting(smartSettingDBModel: SmartSettingDBModel)
}
package com.smartsettings.ai.resources.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SmartSettingDao {

    @Query("SELECT * FROM smart_settings ORDER BY `order`")
    fun getSmartSettings(): LiveData<List<SmartSettingDBModel>>

    @Insert
    fun insertSmartSetting(smartSettingDBModel: SmartSettingDBModel)

    @Update
    fun updateSmartSetting(smartSettingDBModel: SmartSettingDBModel)

    @Delete
    fun deleteSmartSetting(smartSettingDBModel: SmartSettingDBModel)
}
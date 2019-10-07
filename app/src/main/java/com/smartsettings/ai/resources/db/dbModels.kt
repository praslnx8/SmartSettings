package com.smartsettings.ai.resources.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "smart_settings")
data class SmartSettingDBModel(

    @PrimaryKey(autoGenerate = true) var id: Int? = null,

    @ColumnInfo(name = "type") var type: String,

    @ColumnInfo(name = "name") var name: String,

    @ColumnInfo(name = "criteriaData") var serializedCriteriaData: String,

    @ColumnInfo(name = "setting_changers") var settingChangers: List<SettingChangerDBModel>,

    @ColumnInfo(name = "order") var order: Int
)

data class SettingChangerDBModel(
    var type: String,
    var serializedActionData: String
)


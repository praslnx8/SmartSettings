package com.smartsettings.ai.resources.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "smart_settings")
data class SmartSettingDBModel(

    @PrimaryKey(autoGenerate = true) var id: Int? = null,

    @ColumnInfo(name = "name") var name: String,

    @ColumnInfo(name = "setting_changers") var settingChangers: List<SettingChangerDBModel>,

    @ColumnInfo(name = "context_listeners") var contextListeners: List<ContextListenerDBModel>,

    @ColumnInfo(name = "conjunction_logic") var conjunctionLogic: String

)

data class SettingChangerDBModel(
    var type: String,
    var serializedActionData: String
)

data class ContextListenerDBModel(
    var type: String,
    var serializedCriteriaData: String
)


package com.smartsettings.ai.resources.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "smart_settings")
data class SmartSettingDBModel(

    @PrimaryKey(autoGenerate = true) var id: Int,

    @ColumnInfo(name = "type") var type: String,

    @ColumnInfo(name = "enabled") var enabled: Boolean,

    @ColumnInfo(name = "serialized_object") var serializedObject: String,

    @ColumnInfo(name = "order") var order: Int
)


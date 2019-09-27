package com.smartsettings.ai.resources.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "smart_settings")
data class SmartSettingDBModel(

    @PrimaryKey(autoGenerate = true) val id: Int? = null,

    @ColumnInfo(name = "type") val type: String,

    @ColumnInfo(name = "serialized_object") var serializedObject: String,

    @ColumnInfo(name = "order") var order: Int
)


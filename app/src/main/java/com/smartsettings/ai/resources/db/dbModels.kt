package com.smartsettings.ai.resources.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "smart_settings")
data class SmartSettingDBModel(

    @PrimaryKey(autoGenerate = true) var id: Int? = null,

    @ColumnInfo(name = "type") val type: String,

    @ColumnInfo(name = "name") var name: String,

    @ColumnInfo(name = "criteriaData") var serializedCriteriaData: String,

    @ColumnInfo(name = "actionData") var serializedActionData: String,

    @ColumnInfo(name = "order") var order: Int
)


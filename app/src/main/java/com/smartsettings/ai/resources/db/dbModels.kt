package com.smartsettings.ai.resources.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import core.ContextListenerType
import core.SettingChangerType

@Entity(tableName = "smart_settings")
data class SmartSettingDBModel(

    @PrimaryKey(autoGenerate = true) var id: Long? = null,

    @ColumnInfo(name = "name") var name: String,

    @ColumnInfo(name = "setting_changers") var settingChangers: List<SettingChangerDBModel>,

    @ColumnInfo(name = "context_listeners") var contextListeners: List<ContextListenerDBModel>,

    @ColumnInfo(name = "conjunction_logic") var conjunctionLogic: String,

    @ColumnInfo(name = "showNotificationOnTrigger") var showNotificationOnTrigger : Boolean
)

data class SettingChangerDBModel(

    @SerializedName("type")
    var type: SettingChangerType,

    @SerializedName("serializedActionData")
    var serializedActionData: String
)

data class ContextListenerDBModel(

    @SerializedName("type")
    var type: ContextListenerType,

    @SerializedName("serializedCriteriaData")
    var serializedCriteriaData: String
)

@Entity(tableName = "smart_setting_schema", primaryKeys = ["id"])
data class SmartSettingSchemaDBModel(

    @ColumnInfo(name = "id") val id : String,

    @ColumnInfo(name = "title") val title: String,

    @ColumnInfo(name = "description") val description: String?,

    @ColumnInfo(name = "setting_changer_schemas") val settingChangerSchemas: List<SettingChangerSchemaDBModel>,

    @ColumnInfo(name = "context_listener_schemas") val contextListenerSchemas: List<ContextListenerSchemaDBModel>,

    @ColumnInfo(name = "conjunction_logic") val conjunctionLogic: String
)


data class SettingChangerSchemaDBModel(
    @SerializedName("type")
    val type : SettingChangerType,

    @SerializedName("input")
    val input: String?
)


data class ContextListenerSchemaDBModel (
    @SerializedName("type")
    val type : ContextListenerType,

    @SerializedName("input")
    val input: String?
)




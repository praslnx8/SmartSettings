package cloud

import com.google.gson.annotations.SerializedName
import core.ContextListenerType
import core.SettingChangerType

data class SmartSettingSchemaCloudData(

    @SerializedName("title")
    var title: String,

    @SerializedName("description")
    var description: String?,

    @SerializedName("settingChangerSchemas")
    var settingChangerSchemas: List<SettingChangerCloudData>,

    @SerializedName("contextListenerSchemas")
    var contextListenerSchemas: List<ContextListenerCloudData>,

    @SerializedName("conjunctionLogic")
    var conjunctionLogic: String
)


data class SettingChangerCloudData(
    @SerializedName("type")
    val type : SettingChangerType,

    @SerializedName("input")
    val input: String?
)


data class ContextListenerCloudData (
    @SerializedName("type")
    val type : ContextListenerType,

    @SerializedName("input")
    val input: String?
)
package cloud

import com.google.gson.annotations.SerializedName
import core.ContextListenerType
import core.SettingChangerType

data class SmartSettingSchemaCloudData(

    @SerializedName("id")
    val id: String?,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("settingChangerSchemas")
    val settingChangerSchemas: List<SettingChangerCloudData>,

    @SerializedName("contextListenerSchemas")
    val contextListenerSchemas: List<ContextListenerCloudData>,

    @SerializedName("conjunctionLogic")
    val conjunctionLogic: String
) {
    constructor(
        title: String,
        description: String?,
        settingChangerSchemas: List<SettingChangerCloudData>,
        contextListenerSchemas: List<ContextListenerCloudData>,
        conjunctionLogic: String
    ) : this(
        null,
        title,
        description,
        settingChangerSchemas,
        contextListenerSchemas,
        conjunctionLogic
    )
}


data class SettingChangerCloudData(

    @SerializedName("type")
    val type: SettingChangerType,

    @SerializedName("input")
    val input: String?
)


data class ContextListenerCloudData(

    @SerializedName("type")
    val type: ContextListenerType,

    @SerializedName("input")
    val input: String?
)
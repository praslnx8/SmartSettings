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
    val description: String,

    @SerializedName("category")
    val category: SchemaCategory,

    @SerializedName("settingChangerSchemas")
    val settingChangerSchemas: List<SettingChangerCloudData>,

    @SerializedName("contextListenerSchemas")
    val contextListenerSchemas: List<ContextListenerCloudData>,

    @SerializedName("conjunctionLogic")
    val conjunctionLogic: String
) {
    constructor(
        title: String,
        description: String,
        category: SchemaCategory,
        settingChangerSchemas: List<SettingChangerCloudData>,
        contextListenerSchemas: List<ContextListenerCloudData>,
        conjunctionLogic: String
    ) : this(
        null,
        title,
        description,
        category,
        settingChangerSchemas,
        contextListenerSchemas,
        conjunctionLogic
    )
}


data class SettingChangerCloudData(

    @SerializedName("type")
    val type: SettingChangerType,

    @SerializedName("description")
    val description:String,

    @SerializedName("input")
    val input: String?
)


data class ContextListenerCloudData(

    @SerializedName("type")
    val type: ContextListenerType,


    @SerializedName("description")
    val description:String,

    @SerializedName("input")
    val input: String?
)

enum class SchemaCategory {

    LOCATION
}
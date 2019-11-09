package response

import com.google.gson.annotations.SerializedName

data class SmartSettingSchema(

    @SerializedName("title")
    var title: String,

    @SerializedName("description")
    var description: String?,

    @SerializedName("settingChangerSchemas")
    var settingChangerSchemas: List<String>,

    @SerializedName("contextListenerSchemas")
    var contextListenerSchemas: List<String>,

    @SerializedName("conjunctionLogic")
    var conjunctionLogic: String
)
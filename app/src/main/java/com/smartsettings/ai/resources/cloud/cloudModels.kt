package com.smartsettings.ai.resources.cloud

import com.google.gson.annotations.SerializedName

data class SmartSettingSchemaCloudData(

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

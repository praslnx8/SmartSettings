package com.smartsettings.ai.uiModules.smartSettingCreatorView

data class SmartSettingSchemaViewData(

    val id: Int,

    val name: String,

    val description: String?,

    val settingChangerSchemas: List<String>,

    val contextListenerSchemas: List<String>,

    val conjunctionLogic: String
)
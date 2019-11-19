package com.smartsettings.ai.uiModules.smartSettingSchemaChooserView

import core.ContextListenerType
import core.SettingChangerType

data class SmartSettingSchemaViewData(

    val name: String,

    val description: String?,

    val settingChangerSchemas: List<SettingChangerSchemaViewData>,

    val contextListenerSchemas: List<ContextListenerSchemaViewData>,

    val conjunctionLogic: String
)

data class SettingChangerSchemaViewData(
    val type:SettingChangerType,
    val input:String?
)

data class ContextListenerSchemaViewData(
    val type:ContextListenerType,
    val input:String?
)
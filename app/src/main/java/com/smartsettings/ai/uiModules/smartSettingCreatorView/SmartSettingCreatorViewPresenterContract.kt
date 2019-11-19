package com.smartsettings.ai.uiModules.smartSettingCreatorView

import com.smartsettings.ai.uiModules.smartSettingCreatorView.inputView.SmartSettingInputView
import com.smartsettings.ai.uiModules.smartSettingSchemaChooserView.ContextListenerSchemaViewData
import com.smartsettings.ai.uiModules.smartSettingSchemaChooserView.SettingChangerSchemaViewData
import com.smartsettings.ai.uiModules.smartSettingSchemaChooserView.SmartSettingSchemaViewData

interface SmartSettingCreatorView {

    fun addInputView(contextListenerSchemaViewData: ContextListenerSchemaViewData) : SmartSettingInputView<out Any>

    fun addInputView(settingChangerSchemaViewData: SettingChangerSchemaViewData) : SmartSettingInputView<out Any>

    fun getSmartSettingTitle() : String

    fun setSmartSettingData(title : String, description : String?)
}

interface SmartSettingCreatorPresenter {

    fun setView(smartSettingCreatorView: SmartSettingCreatorView)

    fun setSchema(smartSettingShemaViewData: SmartSettingSchemaViewData)

    fun parseSchema()
}
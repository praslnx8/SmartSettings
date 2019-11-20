package com.smartsettings.ai.uiModules.smartSettingCreatorView.inputView

interface SmartSettingInputView<T> {

    fun getInput() : T

    fun validate() : Boolean
}
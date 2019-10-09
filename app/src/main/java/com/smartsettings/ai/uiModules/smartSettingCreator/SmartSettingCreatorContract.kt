package com.smartsettings.ai.uiModules.smartSettingCreator

import com.smartsettings.ai.core.settingChangers.SettingChanger
import com.smartsettings.ai.core.smartSettings.SmartSetting

interface SmartSettingCreatorView {

    fun showChooseSmartSettingMenu()

    fun showInputUIForSmartSettingType(smartSettingType: SmartSettingType)

    fun showChooseSettingChangerMenu()

    fun showSettingChangerInputUI(settingChangerType: SettingChangerType)

    fun close()
}

enum class SmartSettingType {
    LOCATION_BASED_SETTING,
    WIFI_BASED_SETTING,
}

enum class SettingChangerType {
    LOCATION_BASED_SETTING,
    WIFI_BASED_SETTING,
}

interface SmartSettingCreatorPresenter {

    fun setView(view: SmartSettingCreatorView)

    fun onSmartSettingTypeSelected(smartSettingType: SmartSettingType)

    fun onSettingChangerSelected(settingChangerType: SettingChangerType)

    fun onSmartSettingCreated(smartSetting: SmartSetting<out Any>)

    fun onSettingChangersCreated(settingChangers: Set<SettingChanger<Any>>)
}
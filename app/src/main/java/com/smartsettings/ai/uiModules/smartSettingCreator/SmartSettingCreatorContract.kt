package com.smartsettings.ai.uiModules.smartSettingCreator

import com.smartsettings.ai.core.settingChangers.SettingChanger
import com.smartsettings.ai.core.settingChangers.SettingChangerType
import com.smartsettings.ai.core.smartSettings.SmartSetting
import com.smartsettings.ai.core.smartSettings.SmartSettingType

interface SmartSettingCreatorView {

    fun showChooseSmartSettingMenu()

    fun showInputUIForSmartSettingType(smartSettingType: SmartSettingType)

    fun showChooseSettingChangerMenu()

    fun showSettingChangerInputUI(settingChangerType: SettingChangerType)

    fun close()
}

interface SmartSettingCreatorPresenter {

    fun setView(view: SmartSettingCreatorView)

    fun onViewLoaded()

    fun onSmartSettingTypeSelected(smartSettingType: SmartSettingType)

    fun onSettingChangerSelected(settingChangerType: SettingChangerType)

    fun onSmartSettingCreated(smartSetting: SmartSetting<out Any>)

    fun onSettingChangersCreated(settingChangers: Set<SettingChanger<Any>>)
}
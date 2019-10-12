package com.smartsettings.ai.uiModules.uiModels

import com.smartsettings.ai.core.settingChangers.SettingChanger
import com.smartsettings.ai.core.smartSettings.SmartSetting

data class SmartSettingViewData(
    val key: Int,
    var isEnabled: Boolean,
    val name: String,
    val isRunning: Boolean,
    val isChangesApplied: Boolean,
    val isPermissionGranted: Boolean,
    val criteriaData: String,
    val conjunctionLogic: String,
    val settingChangers: List<SettingChangerViewData>
) {
    companion object {
        fun getSmartSetting(smartSettings: List<SmartSetting<out Any>>): List<SmartSettingViewData> {
            val smartSettingViewDataList = ArrayList<SmartSettingViewData>()

            for ((key, smartSetting) in smartSettings.withIndex()) {
                smartSettingViewDataList.add(getSmartSetting(key, smartSetting))
            }

            return smartSettingViewDataList
        }

        fun getSmartSetting(key: Int, smartSetting: SmartSetting<out Any>): SmartSettingViewData {
            return SmartSettingViewData(
                key,
                smartSetting.isEnabled(),
                smartSetting.name,
                smartSetting.isRunning(),
                smartSetting.isChangesApplied(),
                smartSetting.isListeningPermissionGranted(),
                smartSetting.criteriaData.serialize(),
                smartSetting.conjunctionLogic,
                SettingChangerViewData.getSettingChangerViewData(smartSetting.getSettingChangers().toList())
            )
        }
    }
}

data class SettingChangerViewData(
    val serializedData: String,
    val isChangesApplied: Boolean,
    val isPermissionGranted: Boolean
) {

    companion object {
        fun getSettingChangerViewData(settingChangers: List<SettingChanger<out Any>>): List<SettingChangerViewData> {
            val settingChangerViewDataList = ArrayList<SettingChangerViewData>()

            for (settingChanger in settingChangers) {
                settingChangerViewDataList.add(
                    SettingChangerViewData(
                        settingChanger.serializableActionData.serialize(),
                        settingChanger.isChangesApplied(),
                        settingChanger.isPermissionGranted()
                    )
                )
            }

            return settingChangerViewDataList
        }
    }
}
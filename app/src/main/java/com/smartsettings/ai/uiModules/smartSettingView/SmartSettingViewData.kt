package com.smartsettings.ai.uiModules.smartSettingView

import com.smartsettings.ai.core.contextListeners.ContextListener
import com.smartsettings.ai.core.settingChangers.SettingChanger
import com.smartsettings.ai.core.smartSettings.SmartSetting

data class SmartSettingViewData(
    val key: Int,
    var isEnabled: Boolean,
    val name: String,
    val isRunning: Boolean,
    val isChangesApplied: Boolean,
    val isPermissionGranted: Boolean,
    val conjunctionLogic: String,
    val settingChangers: List<SettingChangerViewData>,
    val contextListeners: List<ContextListenerViewData>
) {
    companion object {
        fun getSmartSetting(smartSettings: List<SmartSetting>): List<SmartSettingViewData> {
            val smartSettingViewDataList = ArrayList<SmartSettingViewData>()

            for ((key, smartSetting) in smartSettings.withIndex()) {
                smartSettingViewDataList.add(
                    getSmartSetting(
                        key,
                        smartSetting
                    )
                )
            }

            return smartSettingViewDataList
        }

        fun getSmartSetting(key: Int, smartSetting: SmartSetting): SmartSettingViewData {
            return SmartSettingViewData(
                key,
                smartSetting.isEnabled(),
                smartSetting.name,
                smartSetting.isRunning(),
                smartSetting.isChangesApplied(),
                smartSetting.isListeningPermissionGranted(),
                smartSetting.conjunctionLogic,
                SettingChangerViewData.getSettingChangerViewData(
                    smartSetting.settingChangers.toList()
                ),
                ContextListenerViewData.getContextListenerViewData(
                    smartSetting.contextListeners.toList()
                )
            )
        }
    }
}

data class SettingChangerViewData(
    val serializedActionData: String,
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

data class ContextListenerViewData(
    val serializedCriteriaData: String,
    val isCriteriaMatches: Boolean,
    val isPermissionGranted: Boolean
) {

    companion object {
        fun getContextListenerViewData(settingChangers: List<ContextListener<out Any>>): List<ContextListenerViewData> {
            val contextListenerViewDataList = ArrayList<ContextListenerViewData>()

            for (settingChanger in settingChangers) {
                contextListenerViewDataList.add(
                    ContextListenerViewData(
                        settingChanger.serializableCriteriaData.serialize(),
                        settingChanger.isCriteriaMatches(),
                        settingChanger.isListeningPermissionGranted()
                    )
                )
            }

            return contextListenerViewDataList
        }
    }
}
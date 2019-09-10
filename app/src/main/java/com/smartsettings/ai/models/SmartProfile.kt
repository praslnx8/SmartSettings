package com.smartsettings.ai.models

import androidx.collection.ArrayMap
import com.smartsettings.ai.models.smartSettings.SmartSetting

object SmartProfile {

    private val smartSettings : ArrayMap<SmartSetting, Boolean> = ArrayMap()

    fun enableSmartSetting(smartSetting: SmartSetting) {
        smartSettings.putIfAbsent(smartSetting, true)
        smartSettings[smartSetting] = true
    }

    fun disableSmartSetting(smartSetting: SmartSetting) {
        smartSettings[smartSetting] = false
    }

    fun getSmartSettings(): List<Pair<SmartSetting, Boolean>> {
        val smartSettingsList: ArrayList<Pair<SmartSetting, Boolean>> = ArrayList()

        for ((key, value) in smartSettings) {
            smartSettingsList.add(Pair(key, value))
        }

        return smartSettingsList
    }
}
package com.smartsettings.ai.models

import com.smartsettings.ai.models.smartSettings.SmartSetting

object SmartProfile {

    private val smartSettings: HashSet<SmartSetting<Any>> = HashSet()

    fun enableSmartSetting(smartSetting: SmartSetting<Any>) {
        smartSettings.add(smartSetting)
        smartSetting.setEnabled(true)
    }

    fun disableSmartSetting(smartSetting: SmartSetting<Any>) {
        smartSetting.setEnabled(false)
    }

    fun getSmartSettings(): List<SmartSetting<Any>> {
        return smartSettings.toList()
    }
}
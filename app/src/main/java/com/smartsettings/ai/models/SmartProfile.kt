package com.smartsettings.ai.models

import androidx.collection.ArrayMap
import com.smartsettings.ai.models.smartSettings.SmartSetting

class SmartProfile {

    private val smartSettings : ArrayMap<SmartSetting, Boolean> = ArrayMap()

    fun enableSmartSetting(smartSetting: SmartSetting) {
        smartSettings[smartSetting] = true
    }
}
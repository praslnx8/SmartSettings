package com.smartsettings.ai.models

import com.smartsettings.ai.models.smartSettings.SmartSetting
import com.smartsettings.ai.repositories.SmartSettingRepository

object SmartProfile {

    private val smartSettings: HashSet<SmartSetting<out Any>> = HashSet()

    fun load(smartSettingRepository: SmartSettingRepository, loadedCallback: () -> Unit) {

        smartSettingRepository.getSmartSettings {
            smartSettings.addAll(it)
            loadedCallback()
        }
    }

    fun enableSmartSetting(smartSettingRepository: SmartSettingRepository, smartSetting: SmartSetting<out Any>) {
        if (!smartSettings.contains(smartSetting)) {
            persistSmartSetting(smartSettingRepository, smartSetting)
        }
        smartSettings.add(smartSetting)
        smartSetting.setEnabled(true)
    }

    fun disableSmartSetting(smartSetting: SmartSetting<out Any>) {
        smartSetting.setEnabled(false)
    }

    fun getSmartSettings(): List<SmartSetting<out Any>> {
        return smartSettings.toList()
    }

    private fun persistSmartSetting(
        smartSettingRepository: SmartSettingRepository,
        smartSetting: SmartSetting<out Any>
    ) {
        smartSettingRepository.addSmartSetting(smartSetting)
    }
}
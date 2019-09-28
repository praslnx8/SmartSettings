package com.smartsettings.ai.models

import com.smartsettings.ai.models.smartSettings.SmartSetting
import com.smartsettings.ai.repositories.SmartSettingRepository

object SmartProfile {

    private val smartSettings: HashSet<SmartSetting<out Any, out Any, out Any>> = HashSet()
    private var isLoaded = false

    fun load(smartSettingRepository: SmartSettingRepository, loadedCallback: () -> Unit) {

        synchronized(this) {
            if (isLoaded) {
                loadedCallback()
            } else {
                smartSettingRepository.getSmartSettings {
                    smartSettings.addAll(it)
                    isLoaded = true
                    loadedCallback()
                }
            }
        }
    }

    fun enableSmartSetting(
        smartSettingRepository: SmartSettingRepository,
        smartSetting: SmartSetting<out Any, out Any, out Any>
    ) {
        if (!smartSettings.contains(smartSetting)) {
            persistSmartSetting(smartSettingRepository, smartSetting)
        }
        smartSettings.add(smartSetting)
        smartSetting.setEnabled(true)
    }

    fun disableSmartSetting(smartSetting: SmartSetting<out Any, out Any, out Any>) {
        smartSetting.setEnabled(false)
    }

    fun getSmartSettings(): List<SmartSetting<out Any, out Any, out Any>> {
        return smartSettings.toList()
    }

    private fun persistSmartSetting(
        smartSettingRepository: SmartSettingRepository,
        smartSetting: SmartSetting<out Any, out Any, out Any>
    ) {
        smartSettingRepository.addSmartSetting(smartSetting)
    }
}
package com.smartsettings.ai.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smartsettings.ai.models.smartSettings.SmartSetting
import com.smartsettings.ai.repositories.SmartSettingRepository

object SmartProfile {

    private val smartSettings = HashSet<SmartSetting<out Any, out Any, out Any>>()

    private val smartSettingsLiveData: MutableLiveData<Set<SmartSetting<out Any, out Any, out Any>>> = MutableLiveData(
        smartSettings
    )

    private var isLoaded = false

    private val smartSettingChangeCallback: ((SmartSetting<out Any, out Any, out Any>) -> Unit) = {
        smartSettings.add(it)
        smartSettingsLiveData.value = smartSettings
    }

    fun load(smartSettingRepository: SmartSettingRepository) {

        if (isLoaded) {
            smartSettingsLiveData.value = smartSettings
        } else {
            smartSettingRepository.getSmartSettings {
                for (smartSetting in smartSettings) {
                    smartSetting.setChangesCallback(smartSettingChangeCallback)
                    smartSettings.add(smartSetting)
                }
                smartSettingsLiveData.value = smartSettings
                isLoaded = true
            }
        }
    }

    fun getSmartSettingLiveData(): LiveData<Set<SmartSetting<out Any, out Any, out Any>>> {
        return smartSettingsLiveData
    }

    fun addSmartSetting(
        smartSettingRepository: SmartSettingRepository,
        smartSetting: SmartSetting<out Any, out Any, out Any>
    ) {
        checkAndAdd(smartSetting)
        smartSettingRepository.addSmartSetting(smartSetting)
        smartSettingsLiveData.value = smartSettings
    }

    fun updateSmartSetting(
        smartSettingRepository: SmartSettingRepository,
        smartSetting: SmartSetting<out Any, out Any, out Any>
    ) {
        checkAndAdd(smartSetting)
        smartSettingRepository.updateSmartSetting(smartSetting)
        smartSettingsLiveData.value = smartSettings
    }

    fun deleteSmartSetting(
        smartSettingRepository: SmartSettingRepository,
        smartSetting: SmartSetting<out Any, out Any, out Any>
    ) {
        smartSettings.remove(smartSetting)
        smartSettingRepository.deleteSmartSetting(smartSetting)
        smartSettingsLiveData.value = smartSettings
    }

    private fun checkAndAdd(
        smartSetting: SmartSetting<out Any, out Any, out Any>
    ) {
        if (!smartSettings.contains(smartSetting)) {
            smartSetting.setChangesCallback(smartSettingChangeCallback)
            smartSettings.add(smartSetting)
        }
    }
}
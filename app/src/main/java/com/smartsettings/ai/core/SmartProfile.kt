package com.smartsettings.ai.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smartsettings.ai.core.smartSettings.SmartSetting

object SmartProfile {

    private val smartSettings = HashSet<SmartSetting>()

    private val smartSettingsLiveData: MutableLiveData<Set<SmartSetting>> = MutableLiveData(
        smartSettings
    )

    private val smartSettingsListLiveData: MutableLiveData<Set<SmartSetting>> = MutableLiveData(
        smartSettings
    )

    private var isLoaded = false

    private val smartSettingChangeCallback: ((SmartSetting) -> Unit) = {
        smartSettings.add(it)
        smartSettingsLiveData.postValue(smartSettings)
    }

    private fun updateLiveData() {
        smartSettingsLiveData.value = smartSettings
        smartSettingsListLiveData.value = smartSettings
    }

    fun load(smartSettingRepository: SmartSettingRepository) {

        if (isLoaded) {
            updateLiveData()
        } else {
            smartSettingRepository.getSmartSettings {
                for (smartSetting in it) {
                    smartSetting.setChangesCallback(smartSettingChangeCallback)
                    smartSettings.add(smartSetting)
                }
                updateLiveData()
                isLoaded = true
            }
        }
    }

    fun getSmartSettingLiveData(): LiveData<Set<SmartSetting>> {
        return smartSettingsLiveData
    }

    fun getSmartSettingListLiveData(): LiveData<Set<SmartSetting>> {
        return smartSettingsListLiveData
    }

    fun addSmartSetting(
        smartSettingRepository: SmartSettingRepository,
        smartSetting: SmartSetting
    ) {
        smartSettingRepository.addSmartSetting(smartSetting) {
            smartSetting.id = it
            smartSetting.setChangesCallback(smartSettingChangeCallback)
            smartSettings.add(smartSetting)
            updateLiveData()
        }
    }

    fun updateSmartSetting(
        smartSettingRepository: SmartSettingRepository,
        smartSetting: SmartSetting
    ) {
        smartSettingRepository.updateSmartSetting(smartSetting) {
            smartSetting.setChangesCallback(smartSettingChangeCallback)
            smartSettings.add(smartSetting)
            updateLiveData()
        }
    }

    fun deleteSmartSetting(
        smartSettingRepository: SmartSettingRepository,
        smartSetting: SmartSetting
    ) {
        smartSettingRepository.deleteSmartSetting(smartSetting) {
            smartSettings.remove(smartSetting)
            updateLiveData()
        }
    }
}
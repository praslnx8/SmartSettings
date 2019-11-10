package com.smartsettings.ai.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smartsettings.ai.core.smartSettings.SmartSetting
import com.smartsettings.ai.di.DependencyProvider

object SmartProfile {

    private val smartSettings = HashSet<SmartSetting>()

    private val smartSettingRepository : SmartSettingRepository = DependencyProvider.smartSettingRepository

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

    fun load() {

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
        smartSetting: SmartSetting
    ) {
        smartSettingRepository.updateSmartSetting(smartSetting) {
            smartSetting.setChangesCallback(smartSettingChangeCallback)
            smartSettings.add(smartSetting)
            updateLiveData()
        }
    }

    fun deleteSmartSetting(
        smartSetting: SmartSetting
    ) {
        smartSettingRepository.deleteSmartSetting(smartSetting) {
            smartSettings.remove(smartSetting)
            updateLiveData()
        }
    }
}